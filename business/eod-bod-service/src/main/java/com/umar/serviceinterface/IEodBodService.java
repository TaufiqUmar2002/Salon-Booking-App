package com.umar.serviceinterface;


import com.umar.payload.request.eodbod.EodBodRunRequest;
import com.umar.payload.response.eodbod.EodBodRunResponse;

public interface IEodBodService {

    EodBodRunResponse runEodBod(EodBodRunRequest request);
    void  runHistory();
    void  getEodBodRunDetails(Long runId);
    void  retryEodBod(Long runId);
    void cancelEodBod(Long runId);
    void getActiveRuns();
    void getConfigProcesses();
    void updateConfigProcess(String processKey);
    void  reorderConfigProcesses();
    void reloadConfig();
    void getEodBodRunReport(Long runId);
    void getSchedulerStatus();

}
