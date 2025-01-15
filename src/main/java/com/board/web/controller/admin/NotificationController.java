package com.board.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.board.dto.admin.NotificationRequest;
import com.board.entity.admin.Notification;
import com.board.service.admin.NotificationService;
import com.board.utils.JwtUtil;
import com.board.utils.ResponseUtil;

import java.util.List;
import java.util.Map;

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
        notificationService.createNotification(request.getEventType(), request.getRequestModule(), request.getContent(), request.getContentDetail(), request.getUserId(), request.getEventId());
        return ResponseEntity.ok("알림이 생성되었습니다.");
    }

 // NOTE : 알림 조회 API
    @GetMapping
    public ResponseEntity<?> getNotificationsByUserId() {
        String isAdmin = JwtUtil.getIsAdminFromSecurityContext();
        Long userId = JwtUtil.getUserIdFromSecurityContext();
        
        if(isAdmin.equals("Y")) {
        	return ResponseEntity.ok(notificationService.getNotificationsByAllUser());        	
        }else {
            return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
        }
    }
    
    // NOTE : 특정 사용자 알림 조회 API
    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotificationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    // NOTE : 읽지 않은 알림 조회 API
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    // NOTE : 알림 읽음 처리 API
    @PutMapping("/read")
    public ResponseEntity<?> markAsRead(@RequestBody NotificationRequest request) {
        Long userId = JwtUtil.getUserIdFromSecurityContext();
        String isAdmin = JwtUtil.getIsAdminFromSecurityContext();
        
        if(isAdmin.equals("Y")) {
            notificationService.markAsAdminRead(request.getNotificationIds(), userId);   	
        }else {
            notificationService.markAsRead(request.getNotificationIds(), userId);
        }
        notificationService.markAsRead(request.getNotificationIds(), userId);
        return ResponseEntity.ok(ResponseUtil.successResponse(null));
    }
}
