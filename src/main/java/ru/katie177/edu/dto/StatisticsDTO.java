package ru.katie177.edu.dto;

import lombok.Data;

@Data
public class StatisticsDTO {
    // Общая статистика
    private Long totalUsers;
    private Long totalStudents;
    private Long totalTeachers;
    private Long totalCourses;
    private Long totalActiveEnrollments;

    // Статистика по курсам
    private Long coursesWithEnrollments;
    private Long coursesWithReviews;
    private Double averageCourseRating;

    // Статистика по активности
    private Long totalSubmissions;
    private Long totalQuizSubmissions;
    private Long totalReviews;

    // Статистика по успеваемости
    private Double averageAssignmentScore;
    private Double averageQuizScore;
    private Long completedCourses;

    // Статистика по времени
    private String mostPopularCategory;
    private String mostActiveTeacher;
    private String topRatedCourse;
}