package ru.katie177.edu.service;

import ru.katie177.edu.dto.CourseReviewDTO;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.exception.DuplicateResourceException;
import ru.katie177.edu.model.Course;
import ru.katie177.edu.model.CourseReview;
import ru.katie177.edu.model.User;
import ru.katie177.edu.repository.CourseReviewRepository;
import ru.katie177.edu.repository.UserRepository;
import ru.katie177.edu.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseReviewService {

    private final CourseReviewRepository courseReviewRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseReviewService(CourseReviewRepository courseReviewRepository,
                               UserRepository userRepository,
                               CourseRepository courseRepository) {
        this.courseReviewRepository = courseReviewRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public List<CourseReviewDTO> getAllCourseReviews() {
        return courseReviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CourseReviewDTO getCourseReviewById(Long id) {
        CourseReview review = courseReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв не найден с ID: " + id));
        return convertToDTO(review);
    }

    public List<CourseReviewDTO> getCourseReviewsByCourse(Long courseId) {
        return courseReviewRepository.findByCourseId(courseId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CourseReviewDTO> getCourseReviewsByStudent(Long studentId) {
        return courseReviewRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CourseReviewDTO createCourseReview(CourseReviewDTO reviewDTO) {
        // Проверка на дублирование
        if (courseReviewRepository.findByStudentIdAndCourseId(
                reviewDTO.getStudentId(), reviewDTO.getCourseId()).isPresent()) {
            throw new DuplicateResourceException("Отзыв уже оставлен для этого курса");
        }

        User student = userRepository.findById(reviewDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Студент не найден с ID: " + reviewDTO.getStudentId()));

        Course course = courseRepository.findById(reviewDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Курс не найден с ID: " + reviewDTO.getCourseId()));

        CourseReview review = new CourseReview();
        review.setStudent(student);
        review.setCourse(course);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        CourseReview savedReview = courseReviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    public CourseReviewDTO updateCourseReview(Long id, CourseReviewDTO reviewDTO) {
        CourseReview review = courseReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв не найден с ID: " + id));

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        CourseReview updatedReview = courseReviewRepository.save(review);
        return convertToDTO(updatedReview);
    }

    public void deleteCourseReview(Long id) {
        CourseReview review = courseReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв не найден с ID: " + id));
        courseReviewRepository.delete(review);
    }

    private CourseReviewDTO convertToDTO(CourseReview review) {
        CourseReviewDTO dto = new CourseReviewDTO();
        dto.setId(review.getId());
        dto.setCourseId(review.getCourse().getId());
        dto.setCourseTitle(review.getCourse().getTitle());
        dto.setStudentId(review.getStudent().getId());
        dto.setStudentName(review.getStudent().getName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}