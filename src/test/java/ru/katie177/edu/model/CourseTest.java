package ru.katie177.edu.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {
    
    private Course course;
    private User teacher;
    private Category category;
    private Tag tag1;
    private Tag tag2;
    
    @BeforeEach
    public void setUp() {
        teacher = new User(1L, "Dr. Smith", "dr.smith@university.edu", User.Role.TEACHER, LocalDateTime.now(), null);
        category = new Category();
        category.setId(1L);
        category.setName("Computer Science");
        
        tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Java");
        
        tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Programming");
        
        course = new Course();
        course.setTitle("Advanced Java Programming");
        course.setDescription("Comprehensive course on advanced Java concepts");
        course.setTeacher(teacher);
        course.setCategory(category);
        course.setStartDate(LocalDate.now().plusDays(7));
        course.setDuration(12);
        course.setCreatedAt(LocalDateTime.now().minusDays(2));
        course.setTags(Arrays.asList(tag1, tag2));
    }
    
    @Test
    public void testCourseCreation() {
        assertNotNull(course);
        assertEquals("Advanced Java Programming", course.getTitle());
        assertEquals("Comprehensive course on advanced Java concepts", course.getDescription());
        assertEquals(teacher, course.getTeacher());
        assertEquals(category, course.getCategory());
        assertEquals(LocalDate.now().plusDays(7).toString(), course.getStartDate().toString());
        assertEquals(12, course.getDuration());
        assertNotNull(course.getCreatedAt());
        assertEquals(2, course.getTags().size());
        assertTrue(course.getTags().contains(tag1));
        assertTrue(course.getTags().contains(tag2));
    }
    
    @Test
    public void testCourseSetters() {
        LocalDate newStartDate = LocalDate.now().plusDays(14);
        LocalDateTime now = LocalDateTime.now();
        
        course.setId(1L);
        course.setTitle("Spring Boot Masterclass");
        course.setDescription("Master Spring Boot development");
        course.setStartDate(newStartDate);
        course.setDuration(8);
        course.setCreatedAt(now);
        
        assertEquals(1L, course.getId());
        assertEquals("Spring Boot Masterclass", course.getTitle());
        assertEquals("Master Spring Boot development", course.getDescription());
        assertEquals(newStartDate, course.getStartDate());
        assertEquals(8, course.getDuration());
        assertEquals(now, course.getCreatedAt());
    }
    
    @Test
    public void testNoArgsConstructor() {
        Course emptyCourse = new Course();
        assertNotNull(emptyCourse);
        assertNull(emptyCourse.getId());
        assertNull(emptyCourse.getTitle());
        assertNull(emptyCourse.getDescription());
        assertNull(emptyCourse.getTeacher());
        assertNull(emptyCourse.getCategory());
        assertNull(emptyCourse.getStartDate());
        assertNull(emptyCourse.getDuration());
        assertNull(emptyCourse.getCreatedAt());
        assertNull(emptyCourse.getTags());
        assertNull(emptyCourse.getEnrollments());
        assertNull(emptyCourse.getReviews());
    }
    
    @Test
    public void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate startDate = LocalDate.now().plusDays(7);
        
        Course course = new Course(
            1L, 
            "Test Course", 
            "Test Description", 
            teacher, 
            category, 
            startDate, 
            10, 
            now,
            Arrays.asList(tag1, tag2),
            null,
            null
        );
        
        assertEquals(1L, course.getId());
        assertEquals("Test Course", course.getTitle());
        assertEquals("Test Description", course.getDescription());
        assertEquals(teacher, course.getTeacher());
        assertEquals(category, course.getCategory());
        assertEquals(startDate, course.getStartDate());
        assertEquals(10, course.getDuration());
        assertEquals(now, course.getCreatedAt());
        assertEquals(2, course.getTags().size());
        assertTrue(course.getTags().contains(tag1));
        assertTrue(course.getTags().contains(tag2));
    }
}