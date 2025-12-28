package ru.katie177.edu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answer_options")
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Текст варианта обязателен")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @NotNull(message = "Правильность ответа обязательна")
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @NotNull(message = "Вопрос обязателен")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}