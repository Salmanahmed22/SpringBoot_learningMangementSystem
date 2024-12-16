package com.example.demo.user.instructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InstructorService {
    private InstructorRepository instructorRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public List<Instructor> getInstructors() {
        return instructorRepository.findAll();
    }
}
