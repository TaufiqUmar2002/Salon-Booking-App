package com.umar.payload.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserNotificationRequest {
    private Boolean notifyEmail;
    private Boolean notifySms;
    private Boolean notifyPush;
}
