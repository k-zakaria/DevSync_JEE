package org.capps.repository;

import org.capps.entity.TaskChangeRequest;

import java.util.Optional;

public interface TaskChangeRequestRepository {
    Optional<TaskChangeRequest> findPendingRequestByUserId(int userId);
    void save(TaskChangeRequest request);
}
