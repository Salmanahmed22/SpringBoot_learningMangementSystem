package com.example.demo.service;

import com.example.demo.models.Admin;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Get all admins
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // Get admin by ID
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }


    // Create a new admin
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }


    // Update an existing admin
    public Admin updateAdmin(Long id, Admin updatedAdmin) {
        if (adminRepository.existsByEmailAndIdNot(updatedAdmin.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists for another admin.");
        }

        Admin existingAdmin = adminRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Admin with ID " + id + " not found"));

        if (existingAdmin != null) {
            if (updatedAdmin.getName() != null) {
                existingAdmin.setName(updatedAdmin.getName());
            }

            if (updatedAdmin.getPassword() != null) {
                existingAdmin.setPassword(bCryptPasswordEncoder.encode(updatedAdmin.getPassword()));
            }
            if (updatedAdmin.getEmail() != null) {
                existingAdmin.setEmail(updatedAdmin.getEmail());
            }

            return adminRepository.save(existingAdmin);
        }
        return null;
    }

    // Delete an User
    public void deleteUser(Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            if (user.getRole().equals(Role.STUDENT))
                studentService.deleteStudent(id);
            else if (user.getRole().equals(Role.INSTRUCTOR))
                instructorService.deleteInstructor(id);
            else
                adminRepository.deleteById(id);
        }
    }
}
