package org.capps.service;

public interface UserTokenService {
    boolean hasTokensLeft(int userId);
    boolean checkAndRecordDeletion(int userId);
//    boolean hasMonthlyToken(int userId);
    void useToken(int userId, boolean isMonthly);
    void doubleTokensForUser(int userId);
    Integer getTokensUsed(int userId);
}
