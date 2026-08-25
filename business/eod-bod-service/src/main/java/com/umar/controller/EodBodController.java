package com.umar.controller;

import com.umar.payload.request.eodbod.EodBodRunRequest;
import com.umar.payload.response.eodbod.EodBodRunResponse;
import com.umar.serviceinterface.IEodBodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eod-bod")
@RequiredArgsConstructor
public class EodBodController {

    private final IEodBodService eodBodService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/run")
    public ResponseEntity<EodBodRunResponse> runEodBod(@Valid @RequestBody EodBodRunRequest request) {
        EodBodRunResponse eodBodRunResponse = this.eodBodService.runEodBod(request);
        return ResponseEntity.ok(eodBodRunResponse);
    }

    @GetMapping("/run")
    public ResponseEntity<Void> getRunHistory() {
        this.eodBodService.runHistory();
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/run/{runId}")
    public ResponseEntity<Void> getEodDetails(@PathVariable Long runId) {
        this.eodBodService.getEodBodRunDetails(runId);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/run/{runId}/retry")
    public ResponseEntity<Valid> retryEodBod(@PathVariable Long runId) {
        this.eodBodService.retryEodBod(runId);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/run/{runId}/cancel")
    public ResponseEntity<Void> cancelEodBod(@PathVariable Long runId) {
        this.eodBodService.cancelEodBod(runId);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/runs/active")
    public ResponseEntity<Void> getActiveRuns() {
        this.eodBodService.getActiveRuns();
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/config/processes")
    public ResponseEntity<List<String>> getConfigProcesses() {
        this.eodBodService.getConfigProcesses();
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/config/processes/{processKey}")
    public ResponseEntity<Void> updateConfigProcess(@PathVariable String processKey) {
        this.eodBodService.updateConfigProcess(processKey);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/config/processes/reorder")
    public ResponseEntity<Void> reorderConfigProcesses() {
        this.eodBodService.reorderConfigProcesses();
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/config/reload")
    public ResponseEntity<Void> reloadConfig() {
        this.eodBodService.reloadConfig();
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/runs/{runId}/report")
    public ResponseEntity<Void> getEodReport(@PathVariable Long runId) {
        this.eodBodService.getEodBodRunReport(runId);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/scheduler/status")
    public ResponseEntity<Void> getSchedulerStatus() {
        this.eodBodService.getSchedulerStatus();
        return ResponseEntity.notFound().build();
    }




}
