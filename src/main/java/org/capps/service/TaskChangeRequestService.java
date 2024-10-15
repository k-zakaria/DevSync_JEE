package org.capps.service;

public interface TaskChangeRequestService {
    void createRequest(int userId, int taskId);
    boolean isRequestPendingOver12Hours(int userId);
    void doubleUserTokens(int userId);
}
