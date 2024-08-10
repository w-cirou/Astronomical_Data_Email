package dev.wcirou;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

public class UpdateTableFunctionHandler implements RequestHandler<SNSEvent, String>{
    //Creating Class Wide DynamoDB Client
    private static final AmazonDynamoDB client = AmazonDynamoDBClient
            .builder()
            .withRegion(Regions.US_EAST_2)
            .build();
    //Creating Class Wide DynamoDB Object
    private static final DynamoDB dynamoDB = new DynamoDB(client);

    //Creating a Class Wide services object to access service class methods
    private static final Services services = new Services();
    //Getting table
    private static final Table table = dynamoDB.getTable(System.getenv("TABLE_NAME"));
    //Creating Class Wide SNS Client
    private static final SnsClient sns = SnsClient
            .builder()
            .region(Region.US_EAST_2)
            .build();
    //Creating SNS Topic ARN String
    String topicArn = System.getenv("TOPIC_ARN");
        @Override
        public String handleRequest (SNSEvent input, Context context){
            //Updating table and publishing message to SNS topic, returning a response which communicates the result
            boolean wasUpdateSuccessful;
            boolean wasPublishSuccessful;
            try {
                wasUpdateSuccessful = services.updateTable(dynamoDB,table);
                wasPublishSuccessful = services.publishToSNSTopic(sns, "UpdatedMoonDataTable",topicArn);
            } catch (Exception e) {
                System.out.println("Error caught in catch block for Exception type exception: " + e.getMessage());
                return "Error caught in catch block for Exception type exception: " + e.getMessage();
            }

            if (wasUpdateSuccessful && wasPublishSuccessful){
                return "Successfully Updated Table";
            }else {
                return "Error not caught in catch block, either Table Update or the Topic Publish was unsuccessful";
            }
        }

}
