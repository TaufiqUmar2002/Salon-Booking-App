package com.umar.exchange;

import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name = "SALON-SERVICE",url = "http://localhost:5009")
public interface SalonClient {


}
