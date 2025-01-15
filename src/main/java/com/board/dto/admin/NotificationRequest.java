package com.board.dto.admin;

public class NotificationRequest {
	private String notificationIds;
    private String eventType;
    private String requestModule;
    private String content;
    private String contentDetail;
	private Long userId;
	private Long eventId;

	public String getContentDetail() { return contentDetail; }
	public void setContentDetail(String contentDetail) { this.contentDetail = contentDetail; }

	public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getRequestModule() { return requestModule; }
    public void setRequestModule(String requestModule) { this.requestModule = requestModule; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNotificationIds() { return notificationIds; }
    public void setNotificationIds(String notificationIds) { this.notificationIds = notificationIds; }
    
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    
}