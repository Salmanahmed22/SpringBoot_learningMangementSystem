package com.example.demo.serviceTest;

import com.example.demo.models.Notification;
import com.example.demo.models.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User testUser;
    private Notification testNotification;
    private Notification testUnreadNotification;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        testNotification = new Notification();
        testNotification.setId(1L);
        testNotification.setMessage("Test Notification");
        testNotification.setUser(testUser);
        testNotification.setRead(true);

        testUnreadNotification = new Notification();
        testUnreadNotification.setId(2L);
        testUnreadNotification.setMessage("Unread Notification");
        testUnreadNotification.setUser(testUser);
        testUnreadNotification.setRead(false);
    }

    @Test
    void createNotification_Success() {
        String message = "Test Notification";
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        Notification result = notificationService.createNotification(1L, message);

        assertNotNull(result);
        assertEquals(message, testNotification.getMessage());
        assertEquals(testUser, result.getUser());
        verify(userRepository).findById(1L);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createNotification_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            notificationService.createNotification(1L, "Test Message");
        });

        verify(userRepository).findById(1L);
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void getNotifications_AllNotifications() {
        List<Notification> notifications = Arrays.asList(testNotification, testUnreadNotification);
        when(notificationRepository.findByUserId(1L)).thenReturn(notifications);
        when(notificationRepository.saveAll(any())).thenReturn(notifications);

        List<Notification> result = notificationService.getNotifications(1L, false);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(1).isRead()); // Verify unread notification was marked as read
        verify(notificationRepository).findByUserId(1L);
        verify(notificationRepository).saveAll(notifications);
    }

    @Test
    void getNotifications_UnreadOnly() {
        List<Notification> unreadNotifications = Arrays.asList(testUnreadNotification);
        when(notificationRepository.findByUserIdAndIsRead(1L, false)).thenReturn(unreadNotifications);
        when(notificationRepository.saveAll(any())).thenReturn(unreadNotifications);

        List<Notification> result = notificationService.getNotifications(1L, true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isRead()); // Verify notification was marked as read
        verify(notificationRepository).findByUserIdAndIsRead(1L, false);
        verify(notificationRepository).saveAll(unreadNotifications);
    }

    @Test
    void getNotifications_EmptyList() {
        when(notificationRepository.findByUserId(1L)).thenReturn(Arrays.asList());
        when(notificationRepository.saveAll(any())).thenReturn(Arrays.asList());

        List<Notification> result = notificationService.getNotifications(1L, false);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificationRepository).findByUserId(1L);
        verify(notificationRepository).saveAll(Arrays.asList());
    }

    @Test
    void getNotifications_MarkAsRead() {
        List<Notification> notifications = Arrays.asList(testUnreadNotification);
        when(notificationRepository.findByUserId(1L)).thenReturn(notifications);
        when(notificationRepository.saveAll(any())).thenReturn(notifications);

        List<Notification> result = notificationService.getNotifications(1L, false);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isRead());
        verify(notificationRepository).saveAll(notifications);
    }

    @Test
    void getNotifications_AlreadyRead() {
        List<Notification> notifications = Arrays.asList(testNotification);
        when(notificationRepository.findByUserId(1L)).thenReturn(notifications);
        when(notificationRepository.saveAll(any())).thenReturn(notifications);

        List<Notification> result = notificationService.getNotifications(1L, false);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isRead());
        verify(notificationRepository).saveAll(notifications);
    }
}
