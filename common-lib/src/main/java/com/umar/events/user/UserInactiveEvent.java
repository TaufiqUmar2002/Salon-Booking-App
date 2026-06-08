package com.umar.events.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInactiveEvent {

    private Long userId;

    private String email;

    private LocalDateTime lastActiveAt;

    private Integer inactiveDays;
}
