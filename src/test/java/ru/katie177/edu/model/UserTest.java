package ru.katie177.edu.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    
    private User user;
    
    @BeforeEach
    public void setUp() {
        user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setRole(User.Role.STUDENT);
        user.setCreatedAt(LocalDateTime.now().minusDays(1));
    }
    
    @Test
    public void testUserCreation() {
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(User.Role.STUDENT, user.getRole());
        assertNotNull(user.getCreatedAt());
    }
    
    @Test
    public void testUserSetters() {
        LocalDateTime now = LocalDateTime.now();
        
        user.setId(1L);
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setRole(User.Role.TEACHER);
        user.setCreatedAt(now);
        
        assertEquals(1L, user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane.doe@example.com", user.getEmail());
        assertEquals(User.Role.TEACHER, user.getRole());
        assertEquals(now, user.getCreatedAt());
    }
    
    @Test
    public void testNoArgsConstructor() {
        User emptyUser = new User();
        assertNotNull(emptyUser);
        assertNull(emptyUser.getId());
        assertNull(emptyUser.getName());
        assertNull(emptyUser.getEmail());
        assertNull(emptyUser.getRole());
        assertNull(emptyUser.getCreatedAt());
    }
    
    @Test
    public void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "John Doe", "john@university.edu", User.Role.STUDENT, now, null);
        
        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@university.edu", user.getEmail());
        assertEquals(User.Role.STUDENT, user.getRole());
        assertEquals(now, user.getCreatedAt());
    }
}