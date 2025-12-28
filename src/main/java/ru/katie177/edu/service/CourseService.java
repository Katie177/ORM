package ru.katie177.edu.service;

import ru.katie177.edu.dto.CourseDTO;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.model.Category;
import ru.katie177.edu.model.Course;
import ru.katie177.edu.model.User;
import ru.katie177.edu.repository.CourseRepository;
import ru.katie177.edu.repository.CategoryRepository;
import ru.katie177.edu.repository.CourseReviewRepository;
import ru.katie177.edu.repository.EnrollmentRepository;
import ru.katie177.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseReviewRepository courseReviewRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository,
                         UserRepository userRepository,
                         CategoryRepository categoryRepository,
                         EnrollmentRepository enrollmentRepository,
                         CourseReviewRepository courseReviewRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseReviewRepository = courseReviewRepository;
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Курс не найден с ID: " + id));
        return convertToDTO(course);
    }

    public CourseDTO createCourse(CourseDTO courseDTO) {
        User teacher = userRepository.findById(courseDTO.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Преподаватель не найден с ID: " + courseDTO.getTeacherId()));

        Category category = categoryRepository.findById(courseDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена с ID: " + courseDTO.getCategoryId()));

        Course course = new Course();
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setTeacher(teacher);
        course.setCategory(category);
        course.setStartDate(courseDTO.getStartDate());
        course.setDuration(courseDTO.getDuration());

        Course savedCourse = courseRepository.save(course);
        return convertToDTO(savedCourse);
    }

    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Курс не найден с ID: " + id));

        if (courseDTO.getTeacherId() != null) {
            User teacher = userRepository.findById(courseDTO.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Преподаватель не найден с ID: " + courseDTO.getTeacherId()));
            course.setTeacher(teacher);
        }

        if (courseDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(courseDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена с ID: " + courseDTO.getCategoryId()));
            course.setCategory(category);
        }

        if (courseDTO.getTitle() != null) {
            course.setTitle(courseDTO.getTitle());
        }

        if (courseDTO.getDescription() != null) {
            course.setDescription(courseDTO.getDescription());
        }

        course.setStartDate(courseDTO.getStartDate());
        course.setDuration(courseDTO.getDuration());

        Course updatedCourse = courseRepository.save(course);
        return convertToDTO(updatedCourse);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Курс не найден с ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    public List<CourseDTO> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByCategory(Long categoryId) {
        return courseRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CourseDTO> searchCourses(String keyword) {
        return courseRepository.searchByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setTeacherId(course.getTeacher().getId());
        dto.setTeacherName(course.getTeacher().getName());
        dto.setCategoryId(course.getCategory().getId());
        dto.setCategoryName(course.getCategory().getName());
        dto.setStartDate(course.getStartDate());
        dto.setDuration(course.getDuration());
        dto.setCreatedAt(course.getCreatedAt());

        // Подсчет студентов
        Long studentCount = enrollmentRepository.countByCourseId(course.getId());
        dto.setStudentCount(studentCount != null ? studentCount.intValue() : 0);

        // Средний рейтинг
        Double averageRating = courseReviewRepository.findAverageRatingByCourseId(course.getId()).orElse(0.0);
        dto.setAverageRating(averageRating);

        // Теги
        List<String> tagNames = course.getTags().stream()
                .map(tag -> tag.getName())
                .collect(Collectors.toList());
        dto.setTags(tagNames);

        return dto;
    }
}