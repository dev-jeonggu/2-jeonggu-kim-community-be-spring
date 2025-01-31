package com.board.entity;

import javax.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reg_dt", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime regDt;

    @Column(name = "chg_dt")
    @LastModifiedDate
    private LocalDateTime chgDt;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "image_nm", length = 500)
    private String imageNm;

    // Getters and Setters
}
