package ru.katie177.edu.service;

import ru.katie177.edu.dto.CourseDTO;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.model.Category;
import ru.katie177.edu.model.Course;
import ru.katie177.edu.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.katie177.edu.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseServiceTest {
    
    @Mock
    private CourseRepository courseRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @Mock
    private EnrollmentRepository enrollmentRepository;
    
    @Mock
    private CourseReviewRepository courseReviewRepository;
    
    @InjectMocks
    private CourseService courseService;
    
    private User teacher;
    private Category category;
    private Course course;
    private CourseDTO courseDTO;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        teacher = new User();
        teacher.setId(1L);
        teacher.setName("Dr. Smith");
        teacher.setEmail("dr.smith@university.edu");
        teacher.setRole(User.Role.TEACHER);
        
        category = new Category();
        category.setId(1L);
        category.setName("Computer Science");
        
        course = new Course();
        course.setId(1L);
        course.setTitle("Advanced Java Programming");
        course.setDescription("Comprehensive course on advanced Java concepts");
        course.setTeacher(teacher);
        course.setCategory(category);
        course.setStartDate(LocalDate.now().plusDays(7));
        course.setDuration(12);
        course.setCreatedAt(LocalDateTime.now().minusDays(5));
        course.setTags(new java.util.ArrayList<>());
        
        courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        courseDTO.setTitle("Advanced Java Programming");
        courseDTO.setDescription("Comprehensive course on advanced Java concepts");
        courseDTO.setTeacherId(1L);
        courseDTO.setTeacherName("Dr. Smith");
        courseDTO.setCategoryId(1L);
        courseDTO.setCategoryName("Computer Science");
        courseDTO.setStartDate(LocalDate.now().plusDays(7));
        courseDTO.setDuration(12);
        courseDTO.setCreatedAt(course.getCreatedAt());
        courseDTO.setStudentCount(5);
        courseDTO.setAverageRating(4.5);
        courseDTO.setTags(Arrays.asList("Java", "Programming"));
    }
    
    @Test
    public void testGetAllCourses_ReturnsAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course));
        
        List<CourseDTO> result = courseService.getAllCourses();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Advanced Java Programming", result.get(0).getTitle());
        assertEquals("Dr. Smith", result.get(0).getTeacherName());
        assertEquals("Computer Science", result.get(0).getCategoryName());
        verify(courseRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetCourseById_ReturnsCourseWhenFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.countByCourseId(1L)).thenReturn(5L);
        when(courseReviewRepository.findAverageRatingByCourseId(1L)).thenReturn(Optional.of(4.5));
        
        CourseDTO result = courseService.getCourseById(1L);
        
        assertNotNull(result);
        assertEquals("Advanced Java Programming", result.getTitle());
        assertEquals("Dr. Smith", result.getTeacherName());
        assertEquals("Computer Science", result.getCategoryName());
        assertEquals(5, result.getStudentCount());
        assertEquals(4.5, result.getAverageRating());
        verify(courseRepository, times(1)).findById(1L);
        verify(enrollmentRepository, times(1)).countByCourseId(1L);
        verify(courseReviewRepository, times(1)).findAverageRatingByCourseId(1L);
    }
    
    @Test
    public void testGetCourseById_ThrowsExceptionWhenNotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.getCourseById(999L);
        });
        
        verify(courseRepository, times(1)).findById(999L);
        verify(enrollmentRepository, never()).countByCourseId(anyLong());
        verify(courseReviewRepository, never()).findAverageRatingByCourseId(anyLong());
    }
    
    @Test
    public void testCreateCourse_Success() {
        CourseDTO inputDTO = new CourseDTO();
        inputDTO.setTitle("New Course");
        inputDTO.setDescription("New Course Description");
        inputDTO.setTeacherId(1L);
        inputDTO.setCategoryId(1L);
        inputDTO.setStartDate(LocalDate.now().plusDays(14));
        inputDTO.setDuration(8);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(enrollmentRepository.countByCourseId(1L)).thenReturn(5L);
        when(courseReviewRepository.findAverageRatingByCourseId(1L)).thenReturn(Optional.of(4.5));
        
        CourseDTO result = courseService.createCourse(inputDTO);
        
        assertNotNull(result);
        assertEquals("Advanced Java Programming", result.getTitle());
        assertEquals("Dr. Smith", result.getTeacherName());
        assertEquals("Computer Science", result.getCategoryName());
        verify(userRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }
    
    @Test
    public void testCreateCourse_ThrowsExceptionWhenTeacherNotFound() {
        CourseDTO inputDTO = new CourseDTO();
        inputDTO.setTitle("New Course");
        inputDTO.setDescription("New Course Description");
        inputDTO.setTeacherId(999L);
        inputDTO.setCategoryId(1L);
        
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.createCourse(inputDTO);
        });
        
        verify(userRepository, times(1)).findById(999L);
        verify(categoryRepository, never()).findById(anyLong());
        verify(courseRepository, never()).save(any(Course.class));
    }
    
    @Test
    public void testCreateCourse_ThrowsExceptionWhenCategoryNotFound() {
        CourseDTO inputDTO = new CourseDTO();
        inputDTO.setTitle("New Course");
        inputDTO.setDescription("New Course Description");
        inputDTO.setTeacherId(1L);
        inputDTO.setCategoryId(999L);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.createCourse(inputDTO);
        });
        
        verify(userRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(999L);
        verify(courseRepository, never()).save(any(Course.class));
    }
    
    @Test
    public void testUpdateCourse_Success() {
        CourseDTO updateDTO = new CourseDTO();
        updateDTO.setTitle("Updated Course");
        updateDTO.setDescription("Updated Description");
        updateDTO.setStartDate(LocalDate.now().plusDays(21));
        updateDTO.setDuration(10);
        
        // Создаем обновленный курс для возврата из мока
        Course updatedCourse = new Course();
        updatedCourse.setId(1L);
        updatedCourse.setTitle("Updated Course");
        updatedCourse.setDescription("Updated Description");
        updatedCourse.setTeacher(teacher);
        updatedCourse.setCategory(category);
        updatedCourse.setStartDate(LocalDate.now().plusDays(21));
        updatedCourse.setDuration(10);
        updatedCourse.setCreatedAt(course.getCreatedAt());
        updatedCourse.setTags(new java.util.ArrayList<>());
        
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);
        when(enrollmentRepository.countByCourseId(1L)).thenReturn(5L);
        when(courseReviewRepository.findAverageRatingByCourseId(1L)).thenReturn(Optional.of(4.5));
        
        CourseDTO result = courseService.updateCourse(1L, updateDTO);
        
        assertNotNull(result);
        assertEquals("Updated Course", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }
    
    @Test
    public void testUpdateCourse_ThrowsExceptionWhenCourseNotFound() {
        CourseDTO updateDTO = new CourseDTO();
        updateDTO.setTitle("Updated Course");
        
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.updateCourse(999L, updateDTO);
        });
        
        verify(courseRepository, times(1)).findById(999L);
        verify(courseRepository, never()).save(any(Course.class));
    }
    
    @Test
    public void testUpdateCourse_UpdatesTeacherWhenIdProvided() {
        CourseDTO updateDTO = new CourseDTO();
        updateDTO.setTeacherId(1L);
        
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(enrollmentRepository.countByCourseId(1L)).thenReturn(5L);
        when(courseReviewRepository.findAverageRatingByCourseId(1L)).thenReturn(Optional.of(4.5));
        
        CourseDTO result = courseService.updateCourse(1L, updateDTO);
        
        assertNotNull(result);
        verify(courseRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }
    
    @Test
    public void testUpdateCourse_UpdatesCategoryWhenIdProvided() {
        CourseDTO updateDTO = new CourseDTO();
        updateDTO.setCategoryId(1L);
        
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(enrollmentRepository.countByCourseId(1L)).thenReturn(5L);
        when(courseReviewRepository.findAverageRatingByCourseId(1L)).thenReturn(Optional.of(4.5));
        
        CourseDTO result = courseService.updateCourse(1L, updateDTO);
        
        assertNotNull(result);
        verify(courseRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }
    
    @Test
    public void testDeleteCourse_Success() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1L);
        
        courseService.deleteCourse(1L);
        
        verify(courseRepository, times(1)).existsById(1L);
        verify(courseRepository, times(1)).deleteById(1L);
    }
    
    @Test
    public void testDeleteCourse_ThrowsExceptionWhenCourseNotFound() {
        when(courseRepository.existsById(999L)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.deleteCourse(999L);
        });
        
        verify(courseRepository, times(1)).existsById(999L);
        verify(courseRepository, never()).deleteById(anyLong());
    }
    
    @Test
    public void testGetCoursesByTeacher_ReturnsCoursesByTeacher() {
        when(courseRepository.findByTeacherId(1L)).thenReturn(Arrays.asList(course));
        when(enrollmentRepository.countByCourseId(1L)).thenReturn(5L);
        when(courseReviewRepository.findAverageRatingByCourseId(1L)).thenReturn(Optional.of(4.5));
        
        List<CourseDTO> result = courseService.getCoursesByTeacher(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Advanced Java Programming", result.get(0).getTitle());
        verify(courseRepository, times(1)).findByTeacherId(1L);
    }
    
    @Test
    public void testGetCoursesByCategory_ReturnsCoursesByCategory() {
        when(courseRepository.findByCategoryId(1L)).thenReturn(Arrays.asList(course));
        when(enrollmentRepository.countByCourseId(1L)).thenReturn(5L);
        when(courseReviewRepository.findAverageRatingByCourseId(1L)).thenReturn(Optional.of(4.5));
        
        List<CourseDTO> result = courseService.getCoursesByCategory(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Advanced Java Programming", result.get(0).getTitle());
        verify(courseRepository, times(1)).findByCategoryId(1L);
    }
    
    @Test
    public void testSearchCourses_ReturnsMatchingCourses() {
        when(courseRepository.searchByKeyword("Java")).thenReturn(Arrays.asList(course));
        when(enrollmentRepository.countByCourseId(1L)).thenReturn(5L);
        when(courseReviewRepository.findAverageRatingByCourseId(1L)).thenReturn(Optional.of(4.5));
        
        List<CourseDTO> result = courseService.searchCourses("Java");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Advanced Java Programming", result.get(0).getTitle());
        verify(courseRepository, times(1)).searchByKeyword("Java");
    }
}