package com.board.entity.admin;

import java.time.LocalDateTime;

public class Notification {
    private Long notificationId;
    private String eventType;
    private String content;
    private Long userId;
    private String isRead;
    private LocalDateTime regDt;
    private LocalDateTime chgDt;

    // Getters and Setters
    public Long getNotificationId() { return notificationId; }
    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }
    
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getIsRead() { return isRead; }
    public void setIsRead(String isRead) { this.isRead = isRead; }
    
    public LocalDateTime getRegDt() { return regDt; }
    public void setRegDt(LocalDateTime regDt) { this.regDt = regDt; }
    
    public LocalDateTime getChgDt() { return chgDt; }
    public void setChgDt(LocalDateTime chgDt) { this.chgDt = chgDt; }
}

