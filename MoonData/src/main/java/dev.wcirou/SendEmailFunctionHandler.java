package dev.wcirou;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendEmailFunctionHandler implements RequestHandler<SNSEvent, Void> {
    //Creating Class Wide SES Client with necessary credentials
    private static final SesClient sesClient = SesClient.builder()
            .region(Region.US_EAST_2)
            .build();
    //Creating Email Components
    private static final String sender = "wcirou@students.kennesaw.edu";
    private static final String recipient = "wcirou@students.kennesaw.edu";
    //Getting Logger
    private static final Logger logger = LoggerFactory.getLogger(SendEmailFunctionHandler.class);
    //Creating Class Wide DynamoDB Client
    private static final AmazonDynamoDB client = AmazonDynamoDBClient
            .builder()
            .withRegion(Regions.US_EAST_2)
            .build();
    //Creating Class Wide DynamoDB Object
    private static final DynamoDB dynamoDB = new DynamoDB(client);
    //Getting Table
    private static final Table table = dynamoDB.getTable("MoonData");
    //Creating Services Object to access Methods
    private static final Services services = new Services();

        @Override
        public Void handleRequest(SNSEvent input, Context context) {
            try {
                //Verifying SNS Notification is not null and logging error response if it is
                if (input.getRecords().isEmpty()||input.getRecords()==null){
                    logger.info("Error SNS Event Record is Null");
                }
                //Getting the record from the DynamoDB Stream
                for (SNSEvent.SNSRecord record : input.getRecords()) {
                    if (record.getSNS().getMessage().equals("UpdatedMoonDataTable")||record.getSNS().getMessage().equals("PatchedMoonDataTable")) {
                        //Getting the updated table items by scanning the table
                        Map<String, AttributeValue> updatedItems = services.scanTable(table);
                        //Creating Message to be sent to Emailed
                        String message = "Current Moon Phase: " + updatedItems.get("moonPhase").getS()
                                + "<br>Next Full Moon: " + updatedItems.get("nextFullMoon").getS()
                                + "<br>Next New Moon: " + updatedItems.get("nextNewMoon").getS()
                                + "<br>Next Moon Rise/Set: " + updatedItems.get("nextMoonRise/Set").getS()
                                + "<br>Next Moon Phase: " + updatedItems.get("nextPhase").getS()
                                + "<br>Previous Moon Phase: " + updatedItems.get("previousPhase").getS()
                                + "<br>Moon Direction: " + updatedItems.get("moonDirection").getS()
                                + "<br>Moon Distance from Athens Ga: " + updatedItems.get("moonDistance").getS();
                        String message2 =
                                 "<br>Mercury Visibility: " + updatedItems.get("mercury").getS()
                                + "<br>Venus Visibility: " + updatedItems.get("venus").getS()
                                + "<br>Mars Visibility: " + updatedItems.get("mars").getS()
                                + "<br>Jupiter Visibility: " + updatedItems.get("jupiter").getS()
                                + "<br>Saturn Visibility: " + updatedItems.get("saturn").getS()
                                + "<br>Uranus Visibility: " + updatedItems.get("uranus").getS()
                                + "<br>Neptune Visibility: " + updatedItems.get("neptune").getS();
                        String message3=
                                "<br>Sun Rise Today: " + updatedItems.get("sunRise").getS()
                                + "<br>Sun Set Today: " + updatedItems.get("sunSet").getS()
                                + "<br>UV Level Today: " + updatedItems.get("UV").getS()
                                + "<br>Sun Distance: " + updatedItems.get("sunDistance").getS();

                        String bodyHtml1 = "<html><head></head><body><h1>Current Moon Data</h1><p>" + message + "</p></body></html>";
                        String bodyHtml2 = "<html><head></head><body><h1>Current Planet Visibility Data</h1><p>"+message2+"</p></body></html>";
                        String bodyHtml3 = "<html><head></head><body><h1>Current Sun Data</h1><p>"+message3+"</p></body></html>";
                        //Sending Emails using the sendEmail method included in the Services Class
                        services.sendEmail(sesClient, sender, recipient, "Moon Data Update", message , bodyHtml1);
                        services.sendEmail(sesClient, sender, recipient, "Planet Visibility Update", message2, bodyHtml2);
                        services.sendEmail(sesClient, sender, recipient, "Sun Data Update", message3, bodyHtml3);
                        logger.info("Moon Data was sent to wcirou@students.kennesaw.edu: " + message);
                        logger.info("Planet Visibility Data was sent to wcirou@students.kennesaw.edu: " + message2);
                    } else {
                        logger.info("Error, SNS Event did not contain expected confirmation message that the table was updated");
                    }
                }
            } catch (Exception e) {
                logger.info("Error caught in catch block for Exception type exception: " + e.getMessage());
                System.out.println("Error in catch block: " + e.getMessage());
            }
            return null;
        }
}

