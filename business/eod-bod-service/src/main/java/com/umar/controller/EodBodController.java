package com.umar.controller;

import com.umar.payload.request.eodbod.EodBodRunRequest;
import com.umar.payload.response.eodbod.EodBodRunResponse;
import com.umar.serviceinterface.IEodBodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eod-bod")
@RequiredArgsConstructor
public class EodBodController {

    private final IEodBodService eodBodService;

    @PostMapping("/run")
    public ResponseEntity<EodBodRunResponse> runEodBod(@Valid @RequestBody EodBodRunRequest request) {
        EodBodRunResponse eodBodRunResponse = this.eodBodService.runEodBod(request);
        return ResponseEntity.ok(eodBodRunResponse);
    }

    @PostMapping("/run/{runId}/retry")
    public ResponseEntity<Valid> retryEodBod(@PathVariable Long runId) {
        return null;
    }

    @GetMapping("/config/processes")
    public ResponseEntity<List<String>> getConfigProcesses() {
        return null;
    }
}
