package ru.katie177.edu.repository;

import ru.katie177.edu.model.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    List<CourseReview> findByCourseId(Long courseId);

    List<CourseReview> findByStudentId(Long studentId);

    Optional<CourseReview> findByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("SELECT AVG(cr.rating) FROM CourseReview cr WHERE cr.course.id = :courseId")
    Optional<Double> findAverageRatingByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(cr) FROM CourseReview cr WHERE cr.course.id = :courseId")
    Long countByCourseId(@Param("courseId") Long courseId);
}