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
//
//    @BeforeEach
//    public void initialize_context_logger_and_handler() {
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
