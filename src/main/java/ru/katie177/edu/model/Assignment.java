package ru.katie177.edu.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer maxScore;

    @ManyToOne
    private Lesson lesson;

    @OneToMany(mappedBy = "assignment")
    private List<Submission> submissions;
}
