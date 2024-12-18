package com.example.demo.service;

import com.example.demo.models.Admin;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

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
        Admin existingAdmin = adminRepository.findById(id).orElse(null);
        if (existingAdmin != null) {
            existingAdmin.setName(updatedAdmin.getName());
            existingAdmin.setEmail(updatedAdmin.getEmail());
            existingAdmin.setPassword(updatedAdmin.getPassword());
            return adminRepository.save(existingAdmin);
        }
        return null;
    }

    // Delete an admin
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }
}
