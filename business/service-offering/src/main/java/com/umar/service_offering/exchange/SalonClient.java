package com.umar.service_offering.exchange;

import com.umar.payload.response.salon.SalonResponseV1;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SALON-SERVICE", url = "http://localhost:5009")
public interface SalonClient {

    @GetMapping("/api/salon/{id}")
    SalonResponseV1 getSalonById(@PathVariable("id") Long id);

}
