package com.wechtera.modelPreProcessor.service;

import com.wechtera.modelPreProcessor.model.ModelSpecificInput;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.common.Attributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.stereotype.Service;


import java.util.logging.Logger;


@Service
public class GrpcClient {

    private static final Logger logger = Logger.getLogger(GrpcClient.class.getName());

    /** FOR TESTING PURPOSES
     * ********************
    private Tracer tracer;
    private final ManagedChannel channel;
    private final ModelInferenceServiceGrpc.ModelInferenceServiceBlockingStub stub;
     **********************/

    @Autowired
    public GrpcClient() {

        /** FOR TESTING PURPOSES
         * ********************
        this.channel = ManagedChannelBuilder.forAddress("model-service", 50051)
                .usePlaintext()
                .build();
        this.stub = ModelInferenceServiceGrpc.newBlockingStub(channel);
         **********************/
    }


    public void sendToModel(ModelSpecificInput input, Tracer tracer, Span parentSpan) {
        Span grpcSpan = tracer.spanBuilder("sendToModel")
                .setParent(Context.current().with(parentSpan))
                .startSpan();


        try {
            /** FOR TESTING
             * ********************
            ModelRequest request = ModelRequest.newBuilder()
                    .setData(input.getFieldOne(0))
                    .build();
            stub.infer(request);
             **********************/
            String logMessage = "Sending to model: " + input.getField("Field_1"); // Log the first value of fieldOne (for simplicity)
            logger.info(logMessage);
        }  finally {
            grpcSpan.end();
        }
    }

}
