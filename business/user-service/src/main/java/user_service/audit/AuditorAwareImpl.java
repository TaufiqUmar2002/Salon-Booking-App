package user_service.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import user_service.model.CustomUserDetails;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of("com.umar.user:0");
         }
        CustomUserDetails userDetails =  (CustomUserDetails)authentication.getPrincipal();
        return Optional.of("com.umar.user:"+userDetails.getId());
    }
}
