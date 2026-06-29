package com.umar.repository;

import com.umar.model.UserNotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNotificationPreferenceRepository extends JpaRepository<UserNotificationPreference,Long> {
}
