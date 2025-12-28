package ru.katie177.edu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuizSubmissionDTO {
    private Long id;

    @NotNull(message = "ID теста обязательно")
    private Long quizId;

    @NotNull(message = "ID студента обязательно")
    private Long studentId;

    @NotNull(message = "Балл обязателен")
    private Integer score;

    @NotNull(message = "Процент обязателен")
    private Double percentageScore;

    private LocalDateTime takenAt;
    private Integer timeSpent;
    private String studentName;
    private String quizTitle;
}