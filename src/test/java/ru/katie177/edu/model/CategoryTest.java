package ru.katie177.edu.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {
    
    private Category category;
    private Course course1;
    private Course course2;
    
    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Computer Science");
        
        course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Advanced Java Programming");
        
        course2 = new Course();
        course2.setId(2L);
        course2.setTitle("Spring Boot Masterclass");
        
        category.setCourses(Arrays.asList(course1, course2));
    }
    
    @Test
    public void testCategoryCreation() {
        assertNotNull(category);
        assertEquals(1L, category.getId());
        assertEquals("Computer Science", category.getName());
        assertNotNull(category.getCourses());
        assertEquals(2, category.getCourses().size());
        assertTrue(category.getCourses().contains(course1));
        assertTrue(category.getCourses().contains(course2));
        assertEquals(category, course1.getCategory());
        assertEquals(category, course2.getCategory());
    }
    
    @Test
    public void testCategorySetters() {
        category.setId(2L);
        category.setName("Mathematics");
        
        Course course3 = new Course();
        course3.setId(3L);
        course3.setTitle("Calculus");
        
        category.setCourses(Arrays.asList(course3));
        
        assertEquals(2L, category.getId());
        assertEquals("Mathematics", category.getName());
        assertEquals(1, category.getCourses().size());
        assertEquals("Calculus", category.getCourses().get(0).getTitle());
        assertEquals(category, category.getCourses().get(0).getCategory());
    }
    
    @Test
    public void testNoArgsConstructor() {
        Category emptyCategory = new Category();
        
        assertNotNull(emptyCategory);
        assertNull(emptyCategory.getId());
        assertNull(emptyCategory.getName());
        assertNull(emptyCategory.getCourses());
    }
    
    @Test
    public void testAllArgsConstructor() {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");
        
        List<Course> courses = Arrays.asList(course);
        
        Category category = new Category(1L, "Test Category", courses);
        
        assertEquals(1L, category.getId());
        assertEquals("Test Category", category.getName());
        assertNotNull(category.getCourses());
        assertEquals(1, category.getCourses().size());
        assertEquals("Test Course", category.getCourses().get(0).getTitle());
        assertEquals(category, category.getCourses().get(0).getCategory());
    }
}