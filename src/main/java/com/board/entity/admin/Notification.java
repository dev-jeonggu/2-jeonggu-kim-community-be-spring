package com.board.entity.admin;

import javax.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.board.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) // Auditing 기능 활성화
@Table(name = "notifications") // 테이블 이름 매핑
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 매핑
    @Column(name = "notification_id")
    private Long notificationId; // 알림 ID

    @Column(name = "event_type", nullable = false, length = 20)
    private String eventType; // 이벤트 타입

    @Column(name = "request_module", length = 100)
    private String requestModule; // 요청 모듈

    @Column(nullable = false, length = 200)
    private String content; // 내용

    @Column(name = "content_detail", length = 1000)
    private String contentDetail; // 상세 내용

    @Column(name = "event_id")
    private Integer eventId; // 이벤트 ID

    @ManyToOne(fetch = FetchType.LAZY) // User와의 다대일 관계
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 매핑
    private User user; // 사용자

    @Column(name = "is_read", nullable = false, length = 1)
    private String isRead; // 읽음 여부 ("Y" 또는 "N")

    @Column(name = "is_admin_read", nullable = false, length = 1)
    private String isAdminRead; // 관리자 읽음 여부 ("Y" 또는 "N")

    @Column(name = "notice_id")
    private Integer noticeId; // 공지 ID

    @CreatedDate // 등록 시간
    @Column(name = "reg_dt", nullable = false, updatable = false)
    private LocalDateTime regDt;

    @LastModifiedDate // 수정 시간
    @Column(name = "chg_dt")
    private LocalDateTime chgDt;

    @PrePersist
    public void onCreate() {
        this.regDt = LocalDateTime.now(); // 등록 시간 자동 설정
    }

    @PreUpdate
    public void onUpdate() {
        this.chgDt = LocalDateTime.now(); // 수정 시간 자동 설정
    }
}
