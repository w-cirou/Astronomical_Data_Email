package dev.wcirou;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.concurrent.ScheduledFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//public class updateTableHandlerTest {
//    @Mock
//    SNSEvent input = new SNSEvent();
//    @Mock
//    Context context;
//    @Mock
//    LambdaLogger logger;
//    @InjectMocks
//    UpdateTableFunctionHandler handler;
//    @BeforeEach
//    public void initialize_context_logger_and_handler() {
//        context = mock(Context.class);
//        logger = mock(LambdaLogger.class);
//        handler = new UpdateTableFunctionHandler();
//        when(context.getLogger()).thenReturn(logger);
//    }
//    @Test
//    public void testUpdateTableFunctionHandler() {
//        try {
//            handler.handleRequest(input,context);
//        }catch(Exception e){
//            System.out.println("Error: "+e);
//        }
//    }

//}
