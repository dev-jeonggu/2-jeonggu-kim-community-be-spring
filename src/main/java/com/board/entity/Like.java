package com.board.entity;

import javax.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "likes") // 테이블 이름 매핑
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 매핑
    @Column(name = "like_id")
    private Long likeId; // 기본 키

    @ManyToOne(fetch = FetchType.LAZY) // Board와의 다대일 관계
    @JoinColumn(name = "board_id", nullable = false) // 외래 키 매핑
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY) // User와의 다대일 관계
    @JoinColumn(name = "user_id", nullable = false) // 외래 키 매핑
    private User user;

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
