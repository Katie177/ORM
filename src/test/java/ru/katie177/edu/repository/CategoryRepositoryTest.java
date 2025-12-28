package ru.katie177.edu.repository;

import ru.katie177.edu.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
public class CategoryRepositoryTest extends AbstractRepositoryTest {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    private Category category;
    
    @BeforeEach
    public void setUp() {
        categoryRepository.deleteAll();
        
        category = new Category();
        category.setName("Computer Science");
        
        categoryRepository.save(category);
    }
    
    @Test
    public void testFindByName_ReturnsCategoryWhenFound() {
        Optional<Category> found = categoryRepository.findByName("Computer Science");
        
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Computer Science");
    }
    
    @Test
    public void testFindByName_ReturnsEmptyWhenNotFound() {
        Optional<Category> found = categoryRepository.findByName("Nonexistent Category");
        
        assertThat(found).isEmpty();
    }
    
    @Test
    public void testExistsByName_ReturnsTrueWhenCategoryExists() {
        boolean exists = categoryRepository.existsByName("Computer Science");
        
        assertThat(exists).isTrue();
    }
    
    @Test
    public void testExistsByName_ReturnsFalseWhenCategoryDoesNotExist() {
        boolean exists = categoryRepository.existsByName("Nonexistent Category");
        
        assertThat(exists).isFalse();
    }
    
    @Test
    public void testSaveCategory_PersistsToDatabase() {
        Category newCategory = new Category();
        newCategory.setName("Mathematics");
        
        Category savedCategory = categoryRepository.save(newCategory);
        
        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Mathematics");
        
        Optional<Category> found = categoryRepository.findById(savedCategory.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Mathematics");
    }
}