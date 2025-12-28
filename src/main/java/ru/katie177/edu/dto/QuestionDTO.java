package ru.katie177.edu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class QuestionDTO {
    private Long id;

    @NotBlank(message = "Текст вопроса обязателен")
    private String text;

    @NotNull(message = "Тип вопроса обязателен")
    private String type; // Используем String вместо Question.QuestionType

    @NotNull(message = "ID теста обязательно")
    private Long quizId;

    private List<AnswerOptionDTO> options;

    // Информация для отображения
    private String quizTitle;
}