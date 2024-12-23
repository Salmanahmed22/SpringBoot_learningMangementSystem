package com.example.demo.controller;

import com.example.demo.models.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestParam Long userId, @RequestParam String message) {
        return ResponseEntity.ok(notificationService.createNotification(userId, message));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean unread) {
        return ResponseEntity.ok(notificationService.getNotifications(userId, unread));
    }
}
