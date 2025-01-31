package com.board.entity;

import javax.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) // Auditing 기능 활성화
@Table(name = "users") // 데이터베이스 테이블 이름
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 매핑
    @Column(name = "user_id")
    private Long userId; // 기본 키 (user_id)

    @Column(nullable = false, unique = true, length = 100)
    private String email; // 이메일 (유니크)

    @Column(nullable = false, length = 255)
    private String password; // 비밀번호

    @Column(nullable = false, length = 50)
    private String nickname; // 닉네임

    @Column(name = "profile_url", length = 500)
    private String profileUrl; // 프로필 URL

    @CreatedDate
    @Column(name = "reg_dt", nullable = false, updatable = false)
    private LocalDateTime regDt; // 등록 일시

    @LastModifiedDate
    @Column(name = "chg_dt")
    private LocalDateTime chgDt; // 수정 일시

    @Column(name = "is_admin", nullable = false)
    private String isAdmin; // 관리자 여부
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Board> boards;
}
