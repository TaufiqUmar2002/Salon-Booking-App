package com.umar.exchange;

import com.umar.payload.request.user.UserProfileResponse;
import com.umar.payload.request.user.UserValidateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE",url = "http://localhost:7002")
public interface UserClient {

    @GetMapping("/api/users/validate")
    UserValidateResponse getUserValidation();

    @GetMapping("/profile/{id}")
    UserProfileResponse viewUserProfile(@PathVariable Long id);

}
