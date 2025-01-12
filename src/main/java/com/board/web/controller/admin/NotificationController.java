package com.board.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.board.dto.admin.NotificationRequest;
import com.board.entity.admin.Notification;
import com.board.service.admin.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // NOTE : 알림 생성 API
    @PostMapping
    public ResponseEntity<String> createNotification(@RequestBody NotificationRequest request) {
        notificationService.createNotification(request.getEventType(), request.getRequestModule(), request.getContent(), request.getUserId());
        return ResponseEntity.ok("알림이 생성되었습니다.");
    }

 // NOTE : 전체 사용자 알림 조회 API
    @GetMapping
    public ResponseEntity<List<Notification>> getNotificationsByUserId() {
        return ResponseEntity.ok(notificationService.getNotificationsByAllUser());
    }
    
    // NOTE : 특정 사용자 알림 조회 API
    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    // NOTE : 읽지 않은 알림 조회 API
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    // NOTE : 알림 읽음 처리 API
    @PutMapping("/read/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("알림이 읽음 처리되었습니다.");
    }
}
