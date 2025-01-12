package com.board.dto.admin;

public class NotificationRequest {
    private String eventType;
    private String requestModule;
    private String content;
    private Long userId;

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getRequestModule() { return requestModule; }
    public void setRequestModule(String requestModule) { this.requestModule = requestModule; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}