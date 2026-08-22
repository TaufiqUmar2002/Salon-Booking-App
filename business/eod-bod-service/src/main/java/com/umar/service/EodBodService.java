package com.umar.service;

import com.umar.payload.request.eodbod.EodBodRunRequest;
import com.umar.payload.response.eodbod.EodBodRunResponse;
import com.umar.serviceinterface.IEodBodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EodBodService implements IEodBodService {
    @Override
    public EodBodRunResponse runEodBod(EodBodRunRequest request) {
        return null;
    }
}
