package ru.katie177.edu.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer maxScore;
    private Long lessonId;
    private String lessonTitle;
    private String moduleTitle;
    private String courseTitle;
    private Integer submissionCount;
    private Double averageScore;
}