package org.capps.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "task_change_requests")
@NoArgsConstructor
@Data
public class TaskChangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "request_time", nullable = false)
    private LocalDate requestTime;

    @Column(name = "status", length = 50, nullable = false)
    private String status = "PENDING";

    public TaskChangeRequest(User user, Task task, LocalDate requestTime, String status) {
        this.user = user;
        this.task = task;
        this.requestTime = requestTime;
        this.status = status;
    }
}
