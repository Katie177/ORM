package ru.katie177.edu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Название категории обязательно")
    private String name;

    private Integer courseCount;
}