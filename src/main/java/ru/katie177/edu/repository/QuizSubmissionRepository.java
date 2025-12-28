package ru.katie177.edu.repository;

import ru.katie177.edu.model.QuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    List<QuizSubmission> findByQuizId(Long quizId);

    List<QuizSubmission> findByStudentId(Long studentId);

    Optional<QuizSubmission> findByStudentIdAndQuizId(Long studentId, Long quizId);

    @Query("SELECT AVG(qs.score) FROM QuizSubmission qs WHERE qs.quiz.id = :quizId")
    Optional<Double> findAverageScoreByQuizId(@Param("quizId") Long quizId);

    @Query("SELECT MAX(qs.score) FROM QuizSubmission qs WHERE qs.quiz.id = :quizId")
    Optional<Integer> findMaxScoreByQuizId(@Param("quizId") Long quizId);

    boolean existsByStudentIdAndQuizId(Long studentId, Long quizId);
}