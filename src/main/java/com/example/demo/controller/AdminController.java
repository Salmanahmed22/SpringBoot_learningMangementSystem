//package com.example.demo.controller;
//
//import com.example.demo.models.Admin;
//import com.example.demo.service.AdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admins")
//public class AdminController {
//
//    @Autowired
//    private AdminService adminService;
//
//    // Get all admins
//    @GetMapping
//    public ResponseEntity<List<Admin>> getAllAdmins() {
//        return ResponseEntity.ok(adminService.getAllAdmins());
//    }
//
//    // Get admin by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
//        Admin admin = adminService.getAdminById(id);
//        if (admin == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(admin);
//    }
//
//    // Create a new admin
//    @PostMapping
//    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
//        return ResponseEntity.ok(adminService.createAdmin(admin));
//    }
//
//    // Update an existing admin
//    @PutMapping("/{id}")
//    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin updatedAdmin) {
//        Admin admin = adminService.updateAdmin(id, updatedAdmin);
//        if (admin == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(admin);
//    }
//
//    // Delete an admin
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
//        adminService.deleteAdmin(id);
//        return ResponseEntity.noContent().build();
//    }
//}
