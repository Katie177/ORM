package ru.katie177.edu.service;

import ru.katie177.edu.dto.QuizSubmissionDTO;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.exception.DuplicateResourceException;
import ru.katie177.edu.model.Quiz;
import ru.katie177.edu.model.QuizSubmission;
import ru.katie177.edu.model.User;
import ru.katie177.edu.repository.QuizRepository;
import ru.katie177.edu.repository.QuizSubmissionRepository;
import ru.katie177.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizService {

    private final QuizSubmissionRepository quizSubmissionRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizSubmissionRepository quizSubmissionRepository,
                       UserRepository userRepository,
                       QuizRepository quizRepository) {
        this.quizSubmissionRepository = quizSubmissionRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
    }

    public QuizSubmissionDTO submitQuiz(QuizSubmissionDTO submissionDTO) {
        // Проверка на дублирование
        if (quizSubmissionRepository.existsByStudentIdAndQuizId(
                submissionDTO.getStudentId(), submissionDTO.getQuizId())) {
            throw new DuplicateResourceException("Тест уже пройден");
        }

        User student = userRepository.findById(submissionDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Студент не найден с ID: " + submissionDTO.getStudentId()));

        Quiz quiz = quizRepository.findById(submissionDTO.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Тест не найден с ID: " + submissionDTO.getQuizId()));

        QuizSubmission submission = new QuizSubmission();
        submission.setStudent(student);
        submission.setQuiz(quiz);
        submission.setScore(submissionDTO.getScore());
        submission.setPercentageScore(submissionDTO.getPercentageScore());
        submission.setTimeSpent(submissionDTO.getTimeSpent());

        QuizSubmission savedSubmission = quizSubmissionRepository.save(submission);
        return convertToDTO(savedSubmission);
    }

    public List<QuizSubmissionDTO> getQuizSubmissionsByQuiz(Long quizId) {
        return quizSubmissionRepository.findByQuizId(quizId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<QuizSubmissionDTO> getQuizSubmissionsByStudent(Long studentId) {
        return quizSubmissionRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public QuizSubmissionDTO getQuizSubmissionById(Long id) {
        QuizSubmission submission = quizSubmissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Результат теста не найден с ID: " + id));
        return convertToDTO(submission);
    }

    public Double getAverageScoreForQuiz(Long quizId) {
        return quizSubmissionRepository.findAverageScoreByQuizId(quizId)
                .orElse(0.0);
    }

    public Integer getMaxScoreForQuiz(Long quizId) {
        return quizSubmissionRepository.findMaxScoreByQuizId(quizId)
                .orElse(0);
    }

    private QuizSubmissionDTO convertToDTO(QuizSubmission submission) {
        QuizSubmissionDTO dto = new QuizSubmissionDTO();
        dto.setId(submission.getId());
        dto.setQuizId(submission.getQuiz().getId());
        dto.setQuizTitle(submission.getQuiz().getTitle());
        dto.setStudentId(submission.getStudent().getId());
        dto.setStudentName(submission.getStudent().getName());
        dto.setScore(submission.getScore());
        dto.setPercentageScore(submission.getPercentageScore());
        dto.setTimeSpent(submission.getTimeSpent());
        dto.setTakenAt(submission.getTakenAt());
        return dto;
    }
}