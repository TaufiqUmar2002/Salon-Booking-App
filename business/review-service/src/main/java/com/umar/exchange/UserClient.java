package com.umar.exchange;

import com.umar.payload.request.user.UserValidateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "USER-SERVICE",url = "http://localhost:7002")
public interface UserClient {

    @GetMapping("/api/users/validate")
    UserValidateResponse getUserValidation();

}
