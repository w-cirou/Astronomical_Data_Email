package dev.wcirou;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class Services{
    //Scrapes Moon Data and returns in the form of a HashMap from https://www.timeanddate.com/moon/usa/athens
    public Map<String, String> MoonDataWebScraper(){
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.timeanddate.com/moon/usa/athens").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements elements = doc.select("html > body > div:nth-of-type(5) > main");
        String allData = elements.get(0).text();
        String[] allDataSplit = allData.split(" ");

        //Determining if the moon is rising oro setting next
        String Moon_Rise_or_Set = "";
        if (allDataSplit[56].equals("Moonset:")) {
            Moon_Rise_or_Set = "Sets "+allDataSplit[57]+" at "+allDataSplit[58]+" "+allDataSplit[59];
        }else if (allDataSplit[56].equals("Moonrise:")) {
            Moon_Rise_or_Set = "Rises "+allDataSplit[57]+" at "+allDataSplit[58]+" "+allDataSplit[59];
        }

        Map <String, String> MoonData = new HashMap<>();
        MoonData.put("moonPhase", allDataSplit[17]+" "+allDataSplit[18]);
        MoonData.put("nextPhase", getNextPhase(allDataSplit[17]+" "+allDataSplit[18]));
        MoonData.put("previousPhase", getPreviousPhase(allDataSplit[17]+" "+allDataSplit[18]));
        MoonData.put("nextFullMoon", allDataSplit[42]+" "+allDataSplit[43]+allDataSplit[44]+" "+allDataSplit[45]+" " +allDataSplit[46]);
        MoonData.put("nextNewMoon", allDataSplit[50]+" "+allDataSplit[51]+allDataSplit[52]+" "+allDataSplit[53]+" "+allDataSplit[54]);
        MoonData.put("nextMoonRise/Set", Moon_Rise_or_Set);
        MoonData.put("moonDistance", allDataSplit[37]+" "+allDataSplit[38]);
        MoonData.put("moonDirection", allDataSplit[30]+" "+allDataSplit[31]);
        return MoonData;
    }

    public Map <String, String> PlanetVisibilityWebScrapper(){
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.timeanddate.com/astronomy/night/usa/athens").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements elements = doc.select("body > div.main-content-div > main > article > section.fixed");
        String allData = elements.get(0).text();
        String[] allDataSplit = allData.split(" ");
        Map <String, String> PlanetVisibilityData = new HashMap<>();

        PlanetVisibilityData.put("titleAndDate", allDataSplit[5]+allDataSplit[6]+allDataSplit[7]+allDataSplit[8]+allDataSplit[9]);
        PlanetVisibilityData.put("mercury","Rises on "+ allDataSplit[16]+" at "+allDataSplit[17]+allDataSplit[18]+", Sets on "+ allDataSplit[19]+" at "+allDataSplit[20]+allDataSplit[21]);
        PlanetVisibilityData.put("venus","Rises on "+ allDataSplit[30]+" at "+allDataSplit[31]+allDataSplit[32]+", Sets on "+ allDataSplit[33]+" at "+allDataSplit[34]+allDataSplit[35]);
        PlanetVisibilityData.put("mars","Rises on "+ allDataSplit[44]+" at "+allDataSplit[45]+allDataSplit[46]+", Sets on "+ allDataSplit[47]+" at "+allDataSplit[48]+allDataSplit[49]);
        PlanetVisibilityData.put("jupiter","Rises on "+ allDataSplit[56]+" at "+allDataSplit[57]+allDataSplit[58]+", Sets on "+ allDataSplit[59]+" at "+allDataSplit[60]+allDataSplit[61]);
        PlanetVisibilityData.put("saturn","Rises on "+ allDataSplit[69]+" at "+allDataSplit[70]+allDataSplit[71]+", Sets on "+ allDataSplit[72]+" at "+allDataSplit[73]+allDataSplit[74]);
        PlanetVisibilityData.put("uranus","Rises on "+ allDataSplit[81]+" at "+allDataSplit[82]+allDataSplit[83]+", Sets on "+ allDataSplit[84]+" at "+allDataSplit[85]+allDataSplit[86]);
        PlanetVisibilityData.put("neptune","Rises on "+ allDataSplit[94]+" at "+allDataSplit[95]+allDataSplit[96]+", Sets on "+ allDataSplit[97]+" at "+allDataSplit[98]+allDataSplit[99]);
        return PlanetVisibilityData;
    }
    public Map <String, String> SunDataWebScrapper(){
        Document doc = null;
        try {
            doc = Jsoup.connect("https://uv.willyweather.com/ga/clarke-county/athens.html").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements elements = doc.select("body > section > section.content > aside");
        String UVData = elements.get(0).text();
        String[] UVDataSplit = UVData.split(" ");
        try {
            doc = Jsoup.connect("https://www.timeanddate.com/sun/usa/athens").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        elements = doc.select("body > div.main-content-div > main > article > section.bk-focus > div.bk-focus__info > table");
        String allData = elements.get(0).text();
        String[] allDataSplit = allData.split(" ");

        Map <String, String> sunData = new HashMap<>();
        sunData.put("sunRise", allDataSplit[31]+" "+allDataSplit[32].substring(0,2)+" "+allDataSplit[33]+" "+allDataSplit[34]);
        sunData.put("sunSet", allDataSplit[37]+" "+allDataSplit[38].substring(0,2)+" "+allDataSplit[39]+" "+allDataSplit[40]);
        sunData.put("UV", UVDataSplit[5]+", "+UVDataSplit[6]+" "+UVDataSplit[7]);
        sunData.put("sunDistance", allDataSplit[18]+" "+allDataSplit[19]+" "+allDataSplit[20]);
        return sunData;
    }

    public static String getNextPhase(String currentPhase){
        return switch (currentPhase.toLowerCase()) {
            case "new moon" -> "Waning Crescent";
            case "waxing crescent" -> "New Moon";
            case "first quarter" -> "Waxing Crescent";
            case "waxing gibbous" -> "First Quarter";
            case "full moon" -> "Waxing Gibbous";
            case "waning gibbous" -> "Full Moon";
            case "third quarter" -> "Waning Gibbous";
            case "waning crescent" -> "Third Quarter";
            default -> "Invalid Phase Name";
        };
    }
    public static String getPreviousPhase(String currentPhase){
        return switch (currentPhase.toLowerCase()) {
            case "new moon" -> "Waxing Crescent";
            case "waxing crescent" -> "First Quarter";
            case "first quarter" -> "Waxing Gibbous";
            case "waxing gibbous" -> "Full Moon";
            case "full moon" -> "Waning Gibbous";
            case "waning gibbous" -> "Third Quarter";
            case "third quarter" -> "Waning Crescent";
            case "waning crescent" -> "New Moon";
            default -> "Invalid Phase Name";
        };
    }
    public boolean updateTable(DynamoDB dynamoDB , Table table) {
        //Scrapping Moon Data
        Map<String,String> MoonDataHashMap = MoonDataWebScraper();
        //Scrapping Planet Visibility Data
        Map<String,String> PlanetVisibilityDataHashMap = PlanetVisibilityWebScrapper() ;
        //Scrapping Sun Data
        Map<String,String> SunDataHashMap = SunDataWebScrapper();
        //Creating One Hash Map that is a combination of both results to loop through
        Map<String, String> Moon_Sun_and_Visibility_Data = new HashMap<>();
        Moon_Sun_and_Visibility_Data.putAll(MoonDataHashMap);
        Moon_Sun_and_Visibility_Data.putAll(PlanetVisibilityDataHashMap);
        Moon_Sun_and_Visibility_Data.putAll(SunDataHashMap);

        //Creating Item List by looping through Data
        TableWriteItems tableWriteItems = new TableWriteItems(table.getTableName());
        Item item;
        for (Map.Entry<String, String> entry : Moon_Sun_and_Visibility_Data.entrySet()) {
            item = new Item()
                    .with("Statistic", entry.getKey())
                    .with("Value", entry.getValue());
            if (entry.getKey().contains("next") || entry.getKey().contains("previous")) {
                item.with("PK", "PastOrFutureValues");
            } else {
                item.with("PK", "CurrentValue");
            }
            //Adding Item to List
            tableWriteItems.addItemToPut(item);
        }
        //Sending Item List to DynamoDB Client as a Batch Write Item
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(tableWriteItems);
        return outcome.getBatchWriteItemResult().getSdkHttpMetadata().getHttpStatusCode() == 200;
    }
    public boolean sendEmail(SesClient sesClient, String sender, String recipient, String subject, String bodyText, String bodyHtml) {
        Content subjectContent = Content.builder().data(subject).build();
        Content textBody = Content.builder().data(bodyText).build();
        Content htmlBody = Content.builder().data(bodyHtml).build();
        Body body = Body.builder().text(textBody).html(htmlBody).build();

        Message message = Message.builder()
                .subject(subjectContent)
                .body(body)
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .source(sender)
                .destination(Destination.builder().toAddresses(recipient).build())
                .message(message)
                .build();

        return sesClient.sendEmail(request).sdkHttpResponse().isSuccessful();
    }
    public Map <String, AttributeValue> scanTable(Table table) {
        ItemCollection<ScanOutcome> scanOutcome = table.scan();
        Map <String, AttributeValue> returnMap = new HashMap<>();
        for (Item i : scanOutcome){
            returnMap.put(i.get("Statistic").toString(), new AttributeValue(String.valueOf(i.get("Value"))));
        }
        return returnMap;
    }
    public boolean publishToSNSTopic(SnsClient snsClient, String message, String topicArn){
        PublishRequest request = PublishRequest.builder()
                .message(message)
                .topicArn(topicArn)
                .build();

        PublishResponse result = snsClient.publish(request);
        return result.sdkHttpResponse().isSuccessful();
    }
    public Item getItem(Table table, String partitionKey, String sortKey){
        return table.getItem("PK",partitionKey,"Statistic",sortKey);
    }

}
