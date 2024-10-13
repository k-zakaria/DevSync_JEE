package org.capps.service;

public interface UserTokenService {
    boolean hasTokensLeft(int userId);
    boolean consumeToken(int userId);
    boolean hasMonthlyToken(int userId);
    void useToken(int userId, boolean isMonthly);
}
