package dev.wcirou;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class GetItemFunctionHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
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
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Item item;
        try {
            //Getting Required Path Parameters
            String partitionKey = input.getPathParameters().get("partitionKey");
            String sortKey = input.getPathParameters().get("sortKey");
            item = services.getItem(table, partitionKey, sortKey);
        }catch (Exception e){
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("Error of type Exception caught in catch block");
        }
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(item.toJSON());
    }
}
