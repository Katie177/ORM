package ru.katie177.edu.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseTitle;
    private String status; // Changed from EnrollmentStatus to String
    private LocalDateTime enrollDate;
    private LocalDateTime completedAt;
}