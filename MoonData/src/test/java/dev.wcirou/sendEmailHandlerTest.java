package dev.wcirou;

import static org.mockito.Mockito.mock;
//@ExtendWith(MockitoExtension.class)

//public class sendEmailHandlerTest {
//    Services services = new Services();
//    @Mock
//    SNSEvent input = new SNSEvent();
//    @Mock
//    Context context;
//    @Mock
//    LambdaLogger logger;
//    @InjectMocks
//    SendEmailFunctionHandler handler;
//
//    private Map<String, String> environmentVariables;
//
//    @BeforeEach
//    public void initialize_context_logger_and_handler() {
////        environmentVariables = new HashMap<>();
////        environmentVariables.put("AWS_ACCESS_KEY_ID", "AKIAZQ3DQ3TDQG5HJQSR");
////        environmentVariables.put("AWS_SECRET_ACCESS_KEY", "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");
//
////        when(System.getenv()).thenReturn(environmentVariables);
//        context = mock(Context.class);
//        logger = mock(LambdaLogger.class);
//        handler = new SendEmailFunctionHandler();
//
//    }
//    @Test
//    public void sendEmailHandlerTest() {
//        //Creating Test Stream Record
//        SNSEvent.SNSRecord record = new SNSEvent.SNSRecord();
//        SNSEvent.SNS sns = new SNSEvent.SNS();
//        sns.setMessage("UpdatedMoonDataTable");
//        record.setSns(sns);
//        List<SNSEvent.SNSRecord> records = List.of(record);
//        //Applying sample record to input and testing handleRequest
//        input.setRecords(records);
//        handler.handleRequest(input, context);
//    }
//}
