package dev.wcirou;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

public class PatchTableFunctionHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
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
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        boolean wasUpdateSuccessful;
        boolean wasPublishSuccessful;
        //Patching table and publishing message to SNS topic, returning a response which communicates the result
            try {
                 wasUpdateSuccessful = services.updateTable(dynamoDB,table);
                 wasPublishSuccessful = services.publishToSNSTopic(sns,"PatchedMoonDataTable",topicArn);

            } catch (Exception e) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(400)
                        .withBody("Error caught in catch block for Exception type exception: " + e.getMessage());
            }
            if (wasPublishSuccessful && wasUpdateSuccessful) {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(200)
                        .withBody("Successfully Updated Table and Published Notification to SNS Topic");
            }else {
                return new APIGatewayProxyResponseEvent()
                        .withStatusCode(400)
                        .withBody("Error not caught in catch block, either Table Update or the Topic Publish was unsuccessful");
            }
    }
}
