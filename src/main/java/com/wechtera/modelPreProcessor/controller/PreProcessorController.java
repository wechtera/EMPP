package com.wechtera.modelPreProcessor.controller;

import com.wechtera.modelPreProcessor.util.JSONHelperMethods;
import com.wechtera.modelPreProcessor.validation.ModelInputValidator;
import com.wechtera.modelPreProcessor.model.ModelSpecificInput;
import com.wechtera.modelPreProcessor.service.GrpcClient;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.TraceFlags;
import io.opentelemetry.api.trace.TraceState;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/validate")
public class PreProcessorController {

    private final Logger logger = LoggerFactory.getLogger(PreProcessorController.class);

    //handle some injections
    @Autowired
    private List<ModelInputValidator> validators;

    @Autowired
    private JSONHelperMethods jsonHelperMethods;

    @Autowired
    private GrpcClient grpcClient;

        //so this is officially what grabs the trace id information for context propegation
    private final Tracer tracer = GlobalOpenTelemetry.get().getTracer("example-model-inference", "1.0.0");


    @PostMapping
    public ResponseEntity<String> validateInput(@RequestBody ModelSpecificInput input, @RequestHeader(value = "traceparent", required = false) String traceparent) {
        logger.info("This is a test");

        Span span;

        if(traceparent != null && !traceparent.isEmpty()) {

            //This is the dumbest shit
            String[] parts = traceparent.split("-");
            if (parts.length != 4) {
                throw new IllegalArgumentException("Invalid traceparent format.");
            }
            String traceIdHex = parts[1]; // traceId (16 bytes)
            String spanIdHex = parts[2];  // spanId (8 bytes)
            TraceFlags traceFlags = TraceFlags.fromByte((byte) Integer.parseInt(parts[3].substring(0, 2), 16));
            //ending idiocracy

            SpanContext spanContext = SpanContext.
                    createFromRemoteParent(traceparent, spanIdHex, TraceFlags.getDefault(), TraceState.getDefault());
            span = tracer.spanBuilder("validateInput")
                    .setParent(Context.current().with(Span.wrap(spanContext)))  // Use the parent span context
                    .startSpan();;
        } else {
            //create necessary trace and span
            span = tracer.spanBuilder("processRequest").startSpan();
        }


        /**
         * Run validations below here
         */
        try {
            logger.info("Made it to point 2");

            //all validators are booleans
            List<CompletableFuture<Boolean>> futures = validators.stream()
                    .map(validator -> CompletableFuture.supplyAsync(() -> {
                        //Async Execution
                        Span validationSpan = tracer.spanBuilder(validator.getClass().getSimpleName()).startSpan();
                        try {
                            return validator.validate(input);
                        } finally {
                            validationSpan.end();
                        }
                        //end async execution
                    }))
                    .collect(Collectors.toList());

            boolean isValid = futures.stream()
                    .map(CompletableFuture::join)
                    .allMatch(result -> result);

            //send to input
            if (isValid) {
                grpcClient.sendToModel(input, tracer, span);
                return ResponseEntity.ok("Validation: Success");
            } else {
                return ResponseEntity.badRequest().body("Validation: Failed");
            }
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing input: " + e.getMessage());
        } finally {
            span.end();
        }
    }
}
