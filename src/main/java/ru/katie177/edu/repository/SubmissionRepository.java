package ru.katie177.edu.repository;

import ru.katie177.edu.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignmentId(Long assignmentId);

    List<Submission> findByStudentId(Long studentId);

    List<Submission> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);

    @Query("SELECT s FROM Submission s WHERE s.assignment.lesson.module.course.id = :courseId")
    List<Submission> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT AVG(s.score) FROM Submission s WHERE s.assignment.id = :assignmentId AND s.score > 0")
    Optional<Double> findAverageScoreByAssignmentId(@Param("assignmentId") Long assignmentId);

    boolean existsByStudentIdAndAssignmentId(Long studentId, Long assignmentId);
}