package com.umar.config;

import com.umar.exceptions.user.exceptionController.GlobalException;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Import(GlobalException.class)
@Component
public class NotificationExceptionConfig {
}
