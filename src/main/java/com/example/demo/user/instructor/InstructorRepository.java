package com.example.demo.user.instructor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

}
