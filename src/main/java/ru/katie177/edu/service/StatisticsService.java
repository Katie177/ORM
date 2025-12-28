package ru.katie177.edu.service;

import ru.katie177.edu.dto.StatisticsDTO;
import ru.katie177.edu.model.CourseReview;
import ru.katie177.edu.model.Enrollment;
import ru.katie177.edu.model.User;
import ru.katie177.edu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.katie177.edu.repository.*;

@Service
@Transactional(readOnly = true)
public class StatisticsService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SubmissionRepository submissionRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final CourseReviewRepository courseReviewRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public StatisticsService(UserRepository userRepository,
                             CourseRepository courseRepository,
                             EnrollmentRepository enrollmentRepository,
                             SubmissionRepository submissionRepository,
                             QuizSubmissionRepository quizSubmissionRepository,
                             CourseReviewRepository courseReviewRepository,
                             CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.submissionRepository = submissionRepository;
        this.quizSubmissionRepository = quizSubmissionRepository;
        this.courseReviewRepository = courseReviewRepository;
        this.categoryRepository = categoryRepository;
    }

    public StatisticsDTO getStatistics() {
        StatisticsDTO stats = new StatisticsDTO();

        // Общая статистика
        stats.setTotalUsers(userRepository.count());
        stats.setTotalStudents((long) userRepository.findByRole(User.Role.STUDENT).size());
        stats.setTotalTeachers((long) userRepository.findByRole(User.Role.TEACHER).size());
        stats.setTotalCourses(courseRepository.count());
        stats.setTotalActiveEnrollments(enrollmentRepository.count());

        // Статистика по курсам
        stats.setCoursesWithEnrollments(getCoursesWithEnrollments());
        stats.setCoursesWithReviews(getCoursesWithReviews());
        stats.setAverageCourseRating(getAverageCourseRating());

        // Статистика по активности
        stats.setTotalSubmissions(submissionRepository.count());
        stats.setTotalQuizSubmissions(quizSubmissionRepository.count());
        stats.setTotalReviews(courseReviewRepository.count());

        // Статистика по успеваемости
        stats.setAverageAssignmentScore(getAverageAssignmentScore());
        stats.setAverageQuizScore(getAverageQuizScore());
        stats.setCompletedCourses(getCompletedCourses());

        // Статистика по времени
        stats.setMostPopularCategory(getMostPopularCategory());
        stats.setMostActiveTeacher(getMostActiveTeacher());
        stats.setTopRatedCourse(getTopRatedCourse());

        return stats;
    }

    private Long getCoursesWithEnrollments() {
        return courseRepository.findAll().stream()
                .filter(course -> !course.getEnrollments().isEmpty())
                .count();
    }

    private Long getCoursesWithReviews() {
        return courseRepository.findAll().stream()
                .filter(course -> !course.getReviews().isEmpty())
                .count();
    }

    private Double getAverageCourseRating() {
        return courseRepository.findAll().stream()
                .flatMap(course -> course.getReviews().stream())
                .mapToInt(CourseReview::getRating)
                .average()
                .orElse(0.0);
    }

    private Double getAverageAssignmentScore() {
        return submissionRepository.findAll().stream()
                .filter(submission -> submission.getScore() != null && submission.getScore() > 0)
                .mapToInt(submission -> submission.getScore())
                .average()
                .orElse(0.0);
    }

    private Double getAverageQuizScore() {
        return quizSubmissionRepository.findAll().stream()
                .filter(submission -> submission.getScore() != null && submission.getScore() > 0)
                .mapToInt(submission -> submission.getScore())
                .average()
                .orElse(0.0);
    }

    private Long getCompletedCourses() {
        return enrollmentRepository.findAll().stream()
                .filter(enrollment -> enrollment.getStatus() == Enrollment.EnrollmentStatus.COMPLETED)
                .count();
    }

    private String getMostPopularCategory() {
        return categoryRepository.findAll().stream()
                .max((c1, c2) -> Integer.compare(c1.getCourses().size(), c2.getCourses().size()))
                .map(category -> category.getName())
                .orElse("Нет данных");
    }

    private String getMostActiveTeacher() {
        return userRepository.findAllTeachers().stream()
                .max((t1, t2) -> Integer.compare(t1.getCoursesTaught().size(), t2.getCoursesTaught().size()))
                .map(teacher -> teacher.getName())
                .orElse("Нет данных");
    }

    private String getTopRatedCourse() {
        return courseRepository.findAll().stream()
                .max((c1, c2) -> {
                    Double rating1 = courseReviewRepository.findAverageRatingByCourseId(c1.getId()).orElse(0.0);
                    Double rating2 = courseReviewRepository.findAverageRatingByCourseId(c2.getId()).orElse(0.0);
                    return Double.compare(rating1, rating2);
                })
                .map(course -> course.getTitle())
                .orElse("Нет данных");
    }
}