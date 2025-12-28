package ru.katie177.edu.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProfileDTO {
    private Long id;

    @NotNull(message = "ID пользователя обязательно")
    private Long userId;

    @Size(max = 500, message = "Биография не должна превышать 500 символов")
    private String bio;

    private String avatarUrl;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный номер телефона")
    private String phoneNumber;

    private LocalDate dateOfBirth;

    // For display purposes
    private String userName;
    private String userEmail;
    private String userRole;
}