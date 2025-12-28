package ru.katie177.edu.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private Long teacherId;
    private String teacherName;
    private Long categoryId;
    private String categoryName;
    private LocalDate startDate;
    private Integer duration;
    private Integer studentCount;
    private Double averageRating;
    private List<String> tags;
    private LocalDateTime createdAt;
}