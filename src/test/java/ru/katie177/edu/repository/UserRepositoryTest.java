package ru.katie177.edu.repository;

import ru.katie177.edu.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        
        User student = new User();
        student.setName("John Doe");
        student.setEmail("john.doe@example.com");
        student.setRole(User.Role.STUDENT);
        student.setCreatedAt(LocalDateTime.now().minusDays(5));
        
        User teacher = new User();
        teacher.setName("Dr. Smith");
        teacher.setEmail("dr.smith@university.edu");
        teacher.setRole(User.Role.TEACHER);
        teacher.setCreatedAt(LocalDateTime.now().minusDays(3));
        
        User admin = new User();
        admin.setName("Admin User");
        admin.setEmail("admin@university.edu");
        admin.setRole(User.Role.ADMIN);
        admin.setCreatedAt(LocalDateTime.now().minusDays(1));
        
        userRepository.saveAll(List.of(student, teacher, admin));
    }
    
    @Test
    public void testFindByEmail_ReturnsUserWhenEmailExists() {
        Optional<User> found = userRepository.findByEmail("john.doe@example.com");
        
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("John Doe");
        assertThat(found.get().getRole()).isEqualTo(User.Role.STUDENT);
    }
    
    @Test
    public void testFindByEmail_ReturnsEmptyWhenEmailDoesNotExist() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");
        
        assertThat(found).isEmpty();
    }
    
    @Test
    public void testFindByRole_ReturnsUsersWithMatchingRole() {
        List<User> students = userRepository.findByRole(User.Role.STUDENT);
        List<User> teachers = userRepository.findByRole(User.Role.TEACHER);
        
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getEmail()).isEqualTo("john.doe@example.com");
        
        assertThat(teachers).hasSize(1);
        assertThat(teachers.get(0).getEmail()).isEqualTo("dr.smith@university.edu");
    }
    
    @Test
    public void testFindByRole_ReturnsEmptyListWhenNoMatchingRole() {
        userRepository.deleteAll();
        List<User> students = userRepository.findByRole(User.Role.STUDENT);
        
        assertThat(students).isEmpty();
    }
    
    @Test
    public void testFindAllTeachers_ReturnsAllTeachers() {
        List<User> teachers = userRepository.findAllTeachers();
        
        assertThat(teachers).hasSize(1);
        assertThat(teachers.get(0).getName()).isEqualTo("Dr. Smith");
        assertThat(teachers.get(0).getRole()).isEqualTo(User.Role.TEACHER);
    }
    
    @Test
    public void testFindAllStudents_ReturnsAllStudents() {
        List<User> students = userRepository.findAllStudents();
        
        assertThat(students).hasSize(1);
        assertThat(students.get(0).getName()).isEqualTo("John Doe");
        assertThat(students.get(0).getRole()).isEqualTo(User.Role.STUDENT);
    }
    
    @Test
    public void testExistsByEmail_ReturnsTrueWhenEmailExists() {
        boolean exists = userRepository.existsByEmail("john.doe@example.com");
        
        assertThat(exists).isTrue();
    }
    
    @Test
    public void testExistsByEmail_ReturnsFalseWhenEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
        
        assertThat(exists).isFalse();
    }
    
    @Test
    public void testSaveUser_PersistsToDatabase() {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new.user@example.com");
        newUser.setRole(User.Role.STUDENT);
        
        User savedUser = userRepository.save(newUser);
        
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("New User");
        assertThat(savedUser.getEmail()).isEqualTo("new.user@example.com");
        
        Optional<User> found = userRepository.findById(savedUser.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("New User");
    }
}