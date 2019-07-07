package com.github.victoriaelasticsearch.servicespring;

import com.github.victoriaelasticsearch.api.TraceApi;
import com.github.victoriaelasticsearch.api.model.Trace;
import com.github.victoriaelasticsearch.api.model.TraceReceivedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
public class TraceController implements TraceApi {

    @Override
    public ResponseEntity<Trace> postTrace(UUID xRequestID, @Valid Trace trace) {
        return ResponseEntity.ok(trace);
    }

}
