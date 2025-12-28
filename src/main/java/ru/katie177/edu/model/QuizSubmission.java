package ru.katie177.edu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz_submissions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "quiz_id"})
})
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Тест обязателен")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @NotNull(message = "Студент обязателен")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private Integer score;

    @Column(name = "percentage_score")
    private Double percentageScore;

    @Column(name = "taken_at", nullable = false)
    private LocalDateTime takenAt;

    @Column(name = "time_spent") // в секундах
    private Integer timeSpent;

    @PrePersist
    protected void onCreate() {
        if (takenAt == null) {
            takenAt = LocalDateTime.now();
        }
    }
}