package ru.katie177.edu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SubmissionDTO {
    private Long id;

    @NotNull(message = "ID задания обязательно")
    private Long assignmentId;

    @NotNull(message = "ID студента обязательно")
    private Long studentId;

    @NotBlank(message = "Ответ обязателен")
    private String content;

    private String filePath;
    private Integer score;
    private String feedback;
    private LocalDateTime submittedAt;
    private LocalDateTime gradedAt;
    private String studentName;
    private String assignmentTitle;
}