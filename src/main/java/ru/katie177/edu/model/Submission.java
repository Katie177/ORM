package ru.katie177.edu.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String filePath;
    private Integer score; // Changed from Double to Integer
    private String feedback;
    private LocalDateTime submittedAt = LocalDateTime.now();
    private LocalDateTime gradedAt;

    @ManyToOne
    private Assignment assignment;

    @ManyToOne
    private User student;
}