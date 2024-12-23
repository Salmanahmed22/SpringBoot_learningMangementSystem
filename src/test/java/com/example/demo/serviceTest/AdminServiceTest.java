package com.example.demo.serviceTest;

import com.example.demo.models.Admin;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.InstructorService;
import com.example.demo.service.StudentService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private InstructorService instructorService;

    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAdmins_ShouldReturnAllAdmins() {
        // Arrange
        Admin admin1 = new Admin();
        Admin admin2 = new Admin();
        when(adminRepository.findAll()).thenReturn(Arrays.asList(admin1, admin2));

        // Act
        var result = adminService.getAllAdmins();

        // Assert
        assertEquals(2, result.size());
        verify(adminRepository, times(1)).findAll();
    }

    @Test
    void getAdminById_ShouldReturnAdmin_WhenAdminExists() {
        // Arrange
        Admin admin = new Admin();
        admin.setId(1L);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        // Act
        var result = adminService.getAdminById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(adminRepository, times(1)).findById(1L);
    }

    @Test
    void getAdminById_ShouldReturnNull_WhenAdminDoesNotExist() {
        // Arrange
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        var result = adminService.getAdminById(1L);

        // Assert
        assertNull(result);
        verify(adminRepository, times(1)).findById(1L);
    }

    @Test
    void createAdmin_ShouldSaveAndReturnAdmin() {
        // Arrange
        Admin admin = new Admin();
        when(adminRepository.save(admin)).thenReturn(admin);

        // Act
        var result = adminService.createAdmin(admin);

        // Assert
        assertNotNull(result);
        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    void updateAdmin_ShouldUpdateAndReturnAdmin() {
        // Arrange
        Admin existingAdmin = new Admin();
        existingAdmin.setId(1L);
        existingAdmin.setName("Old Name");

        Admin updatedAdmin = new Admin();
        updatedAdmin.setName("New Name");
        updatedAdmin.setPassword("newpassword");

        when(adminRepository.existsByEmailAndIdNot(anyString(), eq(1L))).thenReturn(false);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(existingAdmin));
        when(bCryptPasswordEncoder.encode("newpassword")).thenReturn("encodedpassword");
        when(adminRepository.save(existingAdmin)).thenReturn(existingAdmin);

        // Act
        var result = adminService.updateAdmin(1L, updatedAdmin);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("encodedpassword", result.getPassword());
        verify(adminRepository, times(1)).save(existingAdmin);
    }

    @Test
    void updateAdmin_ShouldThrowException_WhenEmailExistsForAnotherAdmin() {
        // Arrange
        Admin updatedAdmin = new Admin();
        updatedAdmin.setEmail("existing@example.com");

        when(adminRepository.existsByEmailAndIdNot("existing@example.com", 1L)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> adminService.updateAdmin(1L, updatedAdmin));
        verify(adminRepository, never()).save(any());
    }

    @Test
    void deleteUser_ShouldCallCorrectServiceBasedOnRole() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setRole(Role.STUDENT);

        when(userService.getUserById(1L)).thenReturn(user);

        // Act
        adminService.deleteUser(1L);

        // Assert
        verify(studentService, times(1)).deleteStudent(1L);
        verify(instructorService, never()).deleteInstructor(anyLong());
        verify(adminRepository, never()).deleteById(anyLong());
    }
}
