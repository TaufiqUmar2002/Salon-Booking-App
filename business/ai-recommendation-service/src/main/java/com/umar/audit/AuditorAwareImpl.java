package com.umar.audit;

import com.umar.exchange.UserClient;
import com.umar.payload.request.user.UserValidateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    private final UserClient userClient;

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of("com.umar.user:0");
         }
        UserValidateResponse response=  userClient.getUserValidation();
        if(response!=null && response.getUserId()!=null){
            return Optional.of("com.umar.user:"+response.getUserId());
        }
        return Optional.of("com.umar.user:0");

    }
}
