package ru.katie177.edu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerOptionDTO {
    private Long id;

    @NotBlank(message = "Текст варианта обязателен")
    private String text;

    @NotNull(message = "Правильность ответа обязательна")
    private Boolean isCorrect;

    @NotNull(message = "ID вопроса обязательно")
    private Long questionId;
}