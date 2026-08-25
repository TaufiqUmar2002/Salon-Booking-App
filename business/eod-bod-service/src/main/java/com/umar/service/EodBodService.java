package com.umar.service;

import com.umar.exceptions.common.exception.ApiException;
import com.umar.model.Tenant;
import com.umar.payload.request.eodbod.EodBodRunRequest;
import com.umar.payload.response.eodbod.EodBodRunResponse;
import com.umar.repository.TenantRepository;
import com.umar.serviceinterface.IEodBodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EodBodService implements IEodBodService {


    private final RedissonClient redissonClient;
    private final TenantRepository tenantRepository;

    @Override
    public EodBodRunResponse runEodBod(EodBodRunRequest request) {
        Tenant tenant = tenantRepository.findById(1L).orElseThrow(() -> new RuntimeException("Tenant not found"));
        String runKey = request.getCycleType().name() + "_" + request.getBusinessDate()+"_"+(request.getSalonId()==null?"ALL":request.getSalonId());
        log.info("running eod/bod for cycleType {0} , business Date {1} salon {2}",request.getCycleType(),request.getBusinessDate(),request.getSalonId());
        String lockName = "lock:job-run:" + runKey;
        RLock rLock = redissonClient.getLock(lockName);

        boolean acquired = false;

        try{
            acquired = rLock.tryLock(0,30, TimeUnit.SECONDS);

            if(!acquired){
                throw  new ApiException(HttpStatus.BAD_REQUEST, "Failed to acquire lock","");
            }
        }
        catch (InterruptedException e){

        }
        finally {
            if(acquired && rLock.isHeldByCurrentThread()){
                rLock.unlock();
            }
        }

        return null;
    }

    @Override
    public void runHistory() {

    }

    @Override
    public void getEodBodRunDetails(Long runId) {

    }

    @Override
    public void retryEodBod(Long runId) {

    }

    @Override
    public void cancelEodBod(Long runId) {

    }

    @Override
    public void getActiveRuns() {

    }

    @Override
    public void getConfigProcesses() {

    }

    @Override
    public void updateConfigProcess(String processKey) {

    }

    @Override
    public void reorderConfigProcesses() {

    }

    @Override
    public void reloadConfig() {

    }

    @Override
    public void getEodBodRunReport(Long runId) {

    }

    @Override
    public void getSchedulerStatus() {

    }
}
