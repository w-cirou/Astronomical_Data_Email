package dev.wcirou;

import com.amazonaws.services.lambda.runtime.events.*;
import org.mockito.Mock;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.concurrent.ScheduledFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
//public class patchTableHandlerTest {
//    @Mock
//    APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
//    @Mock
//    Context context;
//    @Mock
//    LambdaLogger logger;
//    @InjectMocks
//    PatchTableFunctionHandler handler;
//    @BeforeEach
//    public void initialize_context_logger_and_handler() {
//        context = mock(Context.class);
//        logger = mock(LambdaLogger.class);
//        handler = new PatchTableFunctionHandler();
//        when(context.getLogger()).thenReturn(logger);
//    }
//    @Test
//    public void testHandleRequest() {
//        handler.handleRequest(input, context);
//    }
//}
