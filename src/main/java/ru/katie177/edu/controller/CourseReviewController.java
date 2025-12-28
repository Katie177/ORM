package ru.katie177.edu.controller;

import ru.katie177.edu.dto.CourseReviewDTO;
import ru.katie177.edu.service.CourseReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-reviews")
@Tag(name = "Отзывы о курсах", description = "Управление отзывами о курсах")
public class CourseReviewController {

    private final CourseReviewService courseReviewService;

    @Autowired
    public CourseReviewController(CourseReviewService courseReviewService) {
        this.courseReviewService = courseReviewService;
    }

    @GetMapping
    @Operation(summary = "Получить все отзывы")
    public ResponseEntity<List<CourseReviewDTO>> getAllCourseReviews() {
        return ResponseEntity.ok(courseReviewService.getAllCourseReviews());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отзыв по ID")
    public ResponseEntity<CourseReviewDTO> getCourseReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(courseReviewService.getCourseReviewById(id));
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Получить отзывы по курсу")
    public ResponseEntity<List<CourseReviewDTO>> getCourseReviewsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseReviewService.getCourseReviewsByCourse(courseId));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Получить отзывы студента")
    public ResponseEntity<List<CourseReviewDTO>> getCourseReviewsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(courseReviewService.getCourseReviewsByStudent(studentId));
    }

    @PostMapping
    @Operation(summary = "Создать новый отзыв")
    public ResponseEntity<CourseReviewDTO> createCourseReview(@Valid @RequestBody CourseReviewDTO reviewDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseReviewService.createCourseReview(reviewDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить отзыв")
    public ResponseEntity<CourseReviewDTO> updateCourseReview(@PathVariable Long id, @Valid @RequestBody CourseReviewDTO reviewDTO) {
        return ResponseEntity.ok(courseReviewService.updateCourseReview(id, reviewDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отзыв")
    public ResponseEntity<Void> deleteCourseReview(@PathVariable Long id) {
        courseReviewService.deleteCourseReview(id);
        return ResponseEntity.noContent().build();
    }
}