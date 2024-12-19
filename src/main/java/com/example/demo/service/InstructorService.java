package com.example.demo.service;

import com.example.demo.models.Instructor;
//import com.example.demo.models.Notification;
//import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    // Get all instructors
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    // Get an instructor by ID
    public Instructor getInstructorById(Long id) {
        Optional<Instructor> instructor = instructorRepository.findById(id);
        return instructor.orElse(null);
    }

    // Create a new instructor
    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    // Update an instructor
    public Instructor updateInstructor(Long id, Instructor updatedInstructor) {
        Optional<Instructor> existingInstructor = instructorRepository.findById(id);
        if (existingInstructor.isPresent()) {
            Instructor instructor = existingInstructor.get();
            instructor.setName(updatedInstructor.getName());
            instructor.setEmail(updatedInstructor.getEmail());
            instructor.setPassword(updatedInstructor.getPassword());
            instructor.setDepartment(updatedInstructor.getDepartment());
            instructor.setEmployeeId(updatedInstructor.getEmployeeId());
            return instructorRepository.save(instructor);
        }
        return null;
    }

    // Delete an instructor
    public void deleteInstructor(Long id) {
        instructorRepository.deleteById(id);
    }


//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    public List<Notification> getNotifications(Long studentId, Boolean unread) {
//        if (unread != null && unread) {
//            return notificationRepository.findByUserIdAndIsRead(studentId, false);
//        }
//        return notificationRepository.findByUserId(studentId);
//    }
//
//    public void markNotificationsAsRead(Long studentId) {
//        List<Notification> notifications = notificationRepository.findByUserIdAndIsRead(studentId, false);
//        for (Notification notification : notifications) {
//            notification.setRead(true);
//        }
//        notificationRepository.saveAll(notifications);
//    }

}
