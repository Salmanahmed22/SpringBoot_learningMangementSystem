//package com.example.demo.controller;
//
//import com.example.demo.models.Notification;
//import com.example.demo.models.NotificationType;
//import com.example.demo.service.NotificationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/notifications")
//public class NotificationController {
//
//    @Autowired
//    private NotificationService notificationService;
//
//    // Create a new notification
//    @PostMapping("/create")
//    public void createNotification(@RequestParam Long recipientId,
//                                   @RequestParam String message,
//                                   @RequestParam NotificationType type) {
//        notificationService.createNotification(recipientId, message, type);
//    }
//
//    // Get unread notifications for a recipient
//    @GetMapping("/unread/{recipientId}")
//    public List<Notification> getUnreadNotifications(@PathVariable Long recipientId) {
//        return notificationService.getUnreadNotifications(recipientId);
//    }
//
//    // Mark a notification as read
//    @PutMapping("/mark-as-read/{notificationId}")
//    public void markNotificationAsRead(@PathVariable Long notificationId) {
//        notificationService.markAsRead(notificationId);
//    }
//}