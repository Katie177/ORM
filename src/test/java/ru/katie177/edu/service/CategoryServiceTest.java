package ru.katie177.edu.service;

import ru.katie177.edu.dto.CategoryDTO;
import ru.katie177.edu.exception.ResourceNotFoundException;
import ru.katie177.edu.model.Category;
import ru.katie177.edu.repository.CategoryRepository;
import ru.katie177.edu.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @Mock
    private CourseRepository courseRepository;
    
    @InjectMocks
    private CategoryService categoryService;
    
    private Category category;
    private CategoryDTO categoryDTO;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        category = new Category();
        category.setId(1L);
        category.setName("Computer Science");
        
        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Computer Science");
        categoryDTO.setCourseCount(5);
    }
    
    @Test
    public void testGetAllCategories_ReturnsAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        when(courseRepository.findByCategoryId(1L)).thenReturn(Arrays.asList());
        
        List<CategoryDTO> result = categoryService.getAllCategories();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Computer Science", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetCategoryById_ReturnsCategoryWhenFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(courseRepository.findByCategoryId(1L)).thenReturn(Arrays.asList());
        
        CategoryDTO result = categoryService.getCategoryById(1L);
        
        assertNotNull(result);
        assertEquals("Computer Science", result.getName());
        assertEquals(0, result.getCourseCount());
        verify(categoryRepository, times(1)).findById(1L);
    }
    
    @Test
    public void testGetCategoryById_ThrowsExceptionWhenNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(999L);
        });
        
        verify(categoryRepository, times(1)).findById(999L);
        verify(courseRepository, never()).findByCategoryId(anyLong());
    }
    
    @Test
    public void testGetCategoryByName_ReturnsCategoryWhenFound() {
        when(categoryRepository.findByName("Computer Science")).thenReturn(Optional.of(category));
        when(courseRepository.findByCategoryId(1L)).thenReturn(Arrays.asList());
        
        CategoryDTO result = categoryService.getCategoryByName("Computer Science");
        
        assertNotNull(result);
        assertEquals("Computer Science", result.getName());
        assertEquals(0, result.getCourseCount());
        verify(categoryRepository, times(1)).findByName("Computer Science");
    }
    
    @Test
    public void testGetCategoryByName_ThrowsExceptionWhenNotFound() {
        when(categoryRepository.findByName("Nonexistent")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryByName("Nonexistent");
        });
        
        verify(categoryRepository, times(1)).findByName("Nonexistent");
        verify(courseRepository, never()).findByCategoryId(anyLong());
    }
    
    @Test
    public void testCreateCategory_Success() {
        CategoryDTO inputDTO = new CategoryDTO();
        inputDTO.setName("Mathematics");
        
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(courseRepository.findByCategoryId(1L)).thenReturn(Arrays.asList());
        
        CategoryDTO result = categoryService.createCategory(inputDTO);
        
        assertNotNull(result);
        assertEquals("Computer Science", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
    
    @Test
    public void testUpdateCategory_Success() {
        CategoryDTO updateDTO = new CategoryDTO();
        updateDTO.setName("Updated Category");
        
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(courseRepository.findByCategoryId(1L)).thenReturn(Arrays.asList());
        
        CategoryDTO result = categoryService.updateCategory(1L, updateDTO);
        
        assertNotNull(result);
        assertEquals("Updated Category", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
    
    @Test
    public void testUpdateCategory_ThrowsExceptionWhenCategoryNotFound() {
        CategoryDTO updateDTO = new CategoryDTO();
        updateDTO.setName("Updated Category");
        
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.updateCategory(999L, updateDTO);
        });
        
        verify(categoryRepository, times(1)).findById(999L);
        verify(categoryRepository, never()).save(any(Category.class));
    }
    
    @Test
    public void testDeleteCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(any(Category.class));
        
        categoryService.deleteCategory(1L);
        
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).delete(any(Category.class));
    }
    
    @Test
    public void testDeleteCategory_ThrowsExceptionWhenCategoryNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.deleteCategory(999L);
        });
        
        verify(categoryRepository, times(1)).findById(999L);
        verify(categoryRepository, never()).delete(any(Category.class));
    }
}