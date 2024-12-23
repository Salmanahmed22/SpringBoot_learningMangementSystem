//package com.example.demo.service;
//
//import com.example.demo.models.Notification;
//import com.example.demo.models.NotificationType;
//import com.example.demo.models.User;
//import com.example.demo.repository.NotificationRepository;
//import com.example.demo.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class NotificationService {
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    @Autowired
//    private UserRepository userRepository; // Inject UserRepository
//
//    // Create a new notification
//    public void createNotification(Long recipientId, String message, NotificationType type) {
//        // Fetch User by recipientId
//        User recipient = userRepository.findById(recipientId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + recipientId));
//
//        Notification notification = new Notification();
//        notification.setRecipient(recipient); // Set the recipient User object
//        notification.setMessage(message);
//        notification.setType(type);
//        notificationRepository.save(notification);
//    }
//
//    // Get all unread notifications for a recipient
//    public List<Notification> getUnreadNotifications(Long recipientId) {
//        return notificationRepository.findByRecipientIdAndIsRead(recipientId, false);
//    }
//
//    // Mark a notification as read
//    public void markAsRead(Long notificationId) {
//        Notification notification = notificationRepository.findById(notificationId)
//                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
//        notification.setRead(true);
//        notificationRepository.save(notification);
//    }
//}