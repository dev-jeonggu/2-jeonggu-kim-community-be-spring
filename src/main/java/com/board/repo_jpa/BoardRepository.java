package com.board.repo_jpa;

import com.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository("jpaBoardRepository")
public interface BoardRepository extends JpaRepository<Board, Long> {

    // NOTE : 특정 게시글 조회 (Map<String, Object> 반환)
    @Query(value = """
            SELECT b.board_id AS boardId, b.title, b.content AS boardContent, b.reg_dt AS date, b.user_id AS userId, b.image_url AS imageUrl,
                   u.email, u.nickname, u.profile_url,
                   (SELECT COUNT(*) FROM likes WHERE board_id = b.board_id) AS likeCnt,
                   (SELECT COUNT(*) FROM comments WHERE board_id = b.board_id) AS commentCnt,
                   (SELECT COUNT(*) FROM boardview WHERE board_id = b.board_id) AS viewCnt
            FROM boards b
            INNER JOIN users u ON b.user_id = u.user_id
            WHERE b.board_id = :boardId
        """, nativeQuery = true)
    Map<String, Object> getBoardById(@Param("boardId") Long boardId);

    // NOTE : 전체 게시글 조회 (List<Map<String, Object>> 반환, 페이징 적용)
    @Query(value = """
            SELECT b.board_id AS boardId, b.title, b.reg_dt AS date, b.user_id AS userId, u.nickname, u.profile_url,
                   (SELECT COUNT(*) FROM likes WHERE board_id = b.board_id) AS likeCnt,
                   (SELECT COUNT(*) FROM comments WHERE board_id = b.board_id) AS commentCnt,
                   (SELECT COUNT(*) FROM boardview WHERE board_id = b.board_id) AS viewCnt
            FROM boards b
            INNER JOIN users u ON b.user_id = u.user_id
            ORDER BY b.reg_dt DESC
            LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<Map<String, Object>> getBoardList(@Param("offset") int offset, @Param("limit") int limit);

    // NOTE : 게시글 검색 (List<Map<String, Object>> 반환)
    @Query(value = """
            SELECT b.board_id AS boardId, b.title, b.content AS boardContent, b.reg_dt AS date, b.user_id AS userId, u.nickname, u.profile_url,
                   (SELECT COUNT(*) FROM likes WHERE board_id = b.board_id) AS likeCnt,
                   (SELECT COUNT(*) FROM comments WHERE board_id = b.board_id) AS commentCnt,
                   (SELECT COUNT(*) FROM boardview WHERE board_id = b.board_id) AS viewCnt
            FROM boards b
            INNER JOIN users u ON b.user_id = u.user_id
            WHERE (:searchKey IS NULL OR :searchKey = '' OR LOWER(b.title) LIKE LOWER(CONCAT('%', :searchValue, '%'))
                   OR LOWER(b.content) LIKE LOWER(CONCAT('%', :searchValue, '%')))
            ORDER BY b.reg_dt DESC
            LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<Map<String, Object>> findAllBySearch(
            @Param("searchKey") String searchKey,
            @Param("searchValue") String searchValue,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    // NOTE : 게시글 수정 (반환값: 업데이트된 행 개수)
    @Transactional
    @Modifying
    @Query("""
        UPDATE Board b
        SET b.title = :title, b.content = :content, b.imageUrl = :imageUrl, b.imageNm = :imageNm, b.chgDt = CURRENT_TIMESTAMP
        WHERE b.boardId = :boardId
    """)
    int editBoard(@Param("boardId") Long boardId, @Param("title") String title,
                  @Param("content") String content, @Param("imageUrl") String imageUrl,
                  @Param("imageNm") String imageNm);

    // NOTE : 게시글 삭제 (반환값: 삭제된 행 개수)
    @Transactional
    @Modifying
    @Query("DELETE FROM Board b WHERE b.boardId = :boardId AND b.user.userId = :userId")
    int deleteBoard(@Param("boardId") Long boardId, @Param("userId") Long userId);

    // NOTE : 게시글 존재 여부 확인
    @Query("SELECT COUNT(b) > 0 FROM Board b WHERE b.boardId = :boardId")
    boolean isBoardExists(@Param("boardId") Long boardId);

    // NOTE : 좋아요 여부 확인
    @Query("""
        SELECT COUNT(l) > 0 FROM Like l
        WHERE l.board.boardId = :boardId AND l.user.userId = :userId
    """)
    boolean isLikeExists(@Param("boardId") Long boardId, @Param("userId") Long userId);

    // NOTE : 좋아요 추가
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO likes (board_id, user_id, reg_dt) VALUES (:boardId, :userId, NOW())", nativeQuery = true)
    void addLike(@Param("boardId") Long boardId, @Param("userId") Long userId);

    // NOTE : 좋아요 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM likes WHERE board_id = :boardId AND user_id = :userId", nativeQuery = true)
    void removeLike(@Param("boardId") Long boardId, @Param("userId") Long userId);

    // NOTE : 좋아요 개수 조회
    @Query("SELECT COUNT(l) FROM Like l WHERE l.board.boardId = :boardId")
    int getLikeCount(@Param("boardId") Long boardId);
}
