package ru.katie177.edu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LessonDTO {
    private Long id;
    private String title;
    private String content;
    private String videoUrl;
    private String resourcesUrl;
    private Long moduleId;
    private String moduleTitle;
    private String courseTitle;
    private Integer assignmentCount;
}