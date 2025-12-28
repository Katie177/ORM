package ru.katie177.edu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModuleDTO {
    private Long id;

    @NotBlank(message = "Название модуля обязательно")
    private String title;

    private String description;

    @NotNull(message = "ID курса обязательно")
    private Long courseId;

    private Integer orderIndex;
    private Integer lessonCount;
    private Boolean hasQuiz;

    private String courseTitle;
}