package com.example.demo.controller;

import com.example.demo.models.Admin;
import com.example.demo.models.User;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController { // All methods had been tested

    @Autowired
    private AdminService adminService;


    // tested but we can ignore it (useless)
    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.createAdmin(admin));
    }


    // Get all admins
    // tested
    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    // Get admin by ID
    //tested
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Admin admin = adminService.getAdminById(id);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(admin);
    }

    // Update an existing admin
    //tested
    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin updatedAdmin) {
        try {
            Admin admin = adminService.updateAdmin(id, updatedAdmin);
            return ResponseEntity.ok(admin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

    }

    // Delete an User
    //tested
    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
