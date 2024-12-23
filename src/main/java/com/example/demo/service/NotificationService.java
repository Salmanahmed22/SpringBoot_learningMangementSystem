package com.example.demo.service;

import com.example.demo.models.Notification;
import com.example.demo.models.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public Notification createNotification(Long userId, String message) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Notification notification = new Notification(message, user);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(Long userId, Boolean unread) {
        List<Notification> notifications;

        // fetch unread
        if (unread != null && unread) {
            notifications = notificationRepository.findByUserIdAndIsRead(userId, false);
        }
        //fetch all
        else {
            notifications = notificationRepository.findByUserId(userId);
        }

        // Mark all retrieved notifications as read
        notifications.forEach(notification -> {
            if (!notification.isRead()) {
                notification.setRead(true);
            }
        });
        notificationRepository.saveAll(notifications);
        return notifications;
    }
}
