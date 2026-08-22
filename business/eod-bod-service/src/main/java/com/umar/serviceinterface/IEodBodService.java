package com.umar.serviceinterface;


import com.umar.payload.request.eodbod.EodBodRunRequest;
import com.umar.payload.response.eodbod.EodBodRunResponse;

public interface IEodBodService {

    EodBodRunResponse runEodBod(EodBodRunRequest request);

}
