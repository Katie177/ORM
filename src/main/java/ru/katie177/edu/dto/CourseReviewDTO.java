package ru.katie177.edu.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CourseReviewDTO {
    private Long id;

    @NotNull(message = "ID курса обязательно")
    private Long courseId;

    @NotNull(message = "ID студента обязательно")
    private Long studentId;

    @Min(value = 1, message = "Рейтинг должен быть от 1 до 5")
    @Max(value = 5, message = "Рейтинг должен быть от 1 до 5")
    @NotNull(message = "Рейтинг обязателен")
    private Integer rating;

    @Size(max = 2000, message = "Комментарий не должен превышать 2000 символов")
    private String comment;

    private LocalDateTime createdAt;

    // Информация для отображения
    private String studentName;
    private String courseTitle;
    private String studentAvatarUrl;
}