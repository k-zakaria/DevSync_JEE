package org.capps.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_tokens",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"}))
@NoArgsConstructor
@Getter
@Setter
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "tokens_used", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int tokensUsed;

    @Column(name = "is_deleted_once_in_month", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeletedOnceInMonth;

    public UserToken(int userId, LocalDate date, int tokensUsed, boolean isDeletedOnceInMonth) {
        this.userId = userId;
        this.date = date;
        this.tokensUsed = tokensUsed;
        this.isDeletedOnceInMonth = isDeletedOnceInMonth;
    }

    public boolean isDeletedOnceInMonth() {
        return isDeletedOnceInMonth;
    }

    public void setDeletedOnceInMonth(boolean deletedOnceInMonth) {
        isDeletedOnceInMonth = deletedOnceInMonth;
    }
}
