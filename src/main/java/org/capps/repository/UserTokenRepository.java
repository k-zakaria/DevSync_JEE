package org.capps.repository;

import org.capps.entity.UserToken;

import java.time.LocalDate;

public interface UserTokenRepository {
    UserToken findByUserIdAndDate(int userId, LocalDate date);
    void save(UserToken token);
    void update(UserToken token);
    Integer getTokensUsed(int userId);
}
