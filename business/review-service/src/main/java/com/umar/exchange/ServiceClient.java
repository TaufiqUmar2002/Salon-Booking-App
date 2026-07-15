package com.umar.exchange;

import com.umar.payload.response.services.ServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SERVICE-OFFERING",url = "http://localhost:5004")
public interface ServiceClient {

    @GetMapping("/api/services/{id}")
    ServiceResponse getServiceById(@PathVariable Long id);
}
