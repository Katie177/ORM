package ru.katie177.edu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuizDTO {
    private Long id;

    @NotBlank(message = "Название теста обязательно")
    private String title;

    private Long courseId;
    private Long moduleId;
    private Integer timeLimit; // в минутах

    private Integer questionCount;
    private Integer submissionCount;
    private Double averageScore;

    // Информация для отображения
    private String courseTitle;
    private String moduleTitle;
}