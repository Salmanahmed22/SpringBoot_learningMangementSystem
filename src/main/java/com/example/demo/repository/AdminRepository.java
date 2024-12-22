package com.example.demo.repository;

import com.example.demo.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Check if an email exists for an admin other than the one being updated
    boolean existsByEmailAndIdNot(String email, Long id);

}
