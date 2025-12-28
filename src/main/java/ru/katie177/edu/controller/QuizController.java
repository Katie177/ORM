package ru.katie177.edu.controller;

import ru.katie177.edu.dto.QuizSubmissionDTO;
import ru.katie177.edu.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@Tag(name = "Тесты", description = "Управление тестами и результатами")
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/submit")
    @Operation(summary = "Отправить результат теста")
    public ResponseEntity<QuizSubmissionDTO> submitQuiz(@Valid @RequestBody QuizSubmissionDTO submissionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(quizService.submitQuiz(submissionDTO));
    }

    @GetMapping("/submissions/{id}")
    @Operation(summary = "Получить результат теста по ID")
    public ResponseEntity<QuizSubmissionDTO> getQuizSubmissionById(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizSubmissionById(id));
    }

    @GetMapping("/{quizId}/submissions")
    @Operation(summary = "Получить результаты теста")
    public ResponseEntity<List<QuizSubmissionDTO>> getQuizSubmissionsByQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getQuizSubmissionsByQuiz(quizId));
    }

    @GetMapping("/student/{studentId}/submissions")
    @Operation(summary = "Получить результаты тестов студента")
    public ResponseEntity<List<QuizSubmissionDTO>> getQuizSubmissionsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(quizService.getQuizSubmissionsByStudent(studentId));
    }

    @GetMapping("/{quizId}/average")
    @Operation(summary = "Получить средний балл по тесту")
    public ResponseEntity<Double> getAverageScoreForQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getAverageScoreForQuiz(quizId));
    }

    @GetMapping("/{quizId}/max")
    @Operation(summary = "Получить максимальный балл по тесту")
    public ResponseEntity<Integer> getMaxScoreForQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getMaxScoreForQuiz(quizId));
    }
}
