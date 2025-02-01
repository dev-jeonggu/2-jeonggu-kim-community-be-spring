package com.board.service;

import com.board.dto.BoardListResponse;
import com.board.dto.BoardRequest;
import com.board.dto.BoardResponse;
import com.board.repo_jdbc.admin.NotificationRepository;
import com.board.repo_jdbc.BoardRepository;
import com.board.repo_jdbc.CommentRepository;
import com.board.utils.LoggerUtil;
import com.board.utils.ResponseUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {
	@Autowired
	private NotificationRepository notificationRepository;

	@Qualifier("jdbcBoardRepository")
	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;

	// NOTE : 게시글 목록 조회
	public Map<String,Object> getBoardList(int currentPage, String searchKey, String searchValue) {
		int offset = (currentPage - 1) * 5; // NOTE : 페이지네이션 계산
		int limit = 5;
		List<Map<String, Object>> boardList = boardRepository.findAllBySearch(searchKey, searchValue, offset, limit);

        boolean hasMore = boardList.size() > limit;
        if (hasMore) {
        	boardList.remove(boardList.size() - 1);
        }

        // 결과 반환 (Node.js 구조에 맞춤)
        Map<String, Object> result = new HashMap<>();
        result.put("data", boardList);
        result.put("hasMore", hasMore);


		return result;
	}

	// NOTE : 게시글 추가
	public Map<String, Object> addBoard(BoardRequest request, Long userId) {
		Long result = boardRepository.addBoard(request.getTitle(), request.getContent(), userId, request.getImageUrl(), request.getImageNm());

		if (result > 0) {
			notificationRepository.save("CREATE", "boards", "게시글이 등록되었습니다.", request.getTitle(), userId, result);

			return ResponseUtil.successResponse(null);
		} else {
			throw new RuntimeException("Failed to add board");
		}
	}


	// NOTE : 좋아요 추가
	// ADD : 추후에 해당 부분 알림 추가
	public Map<String, Object> likeBoard(Long userId, Long boardId) {
        if (!boardRepository.isBoardExists(boardId)) {
            throw new IllegalArgumentException("Board does not exist.");
        }

        boolean isLikeExists = boardRepository.isLikeExists(boardId, userId);
        if (isLikeExists) {
        	boardRepository.removeLike(boardId, userId);
        } else {
        	boardRepository.addLike(boardId, userId);
        }

        int likeCount = boardRepository.getLikeCount(boardId);
        Map<String, Object> response = new HashMap<>();
        response.put("like_cnt", likeCount);
        response.put("liked", !isLikeExists);
        
        return ResponseUtil.successResponse(response);
	}
	
	// NOTE : 게시글 상세 조회
	public Map<String, Object> getBoardInfo(Long boardId, Long userId, String url) {
		Map<String, Object> board = boardRepository.findById(boardId, userId, url);
		
		if (board.isEmpty()) {
			LoggerUtil.debug("Board not found");
			return ResponseUtil.errorResponse(null);
		}

		return ResponseUtil.successResponse(board);
	}

	// NOTE : 게시글 수정
	public Map<String, Object> editBoard(Long boardId, BoardRequest request) {
		int result = boardRepository.editBoard(boardId, request.getTitle(), request.getContent(), request.getImageUrl(),
				request.getImageNm(), request.getUserId());

		if (result > 0) {
			notificationRepository.save("UPDATE", "boards", "게시글이 수정되었습니다.", request.getTitle(), request.getUserId(), boardId);
			return ResponseUtil.successResponse(null);
		} else {
			throw new RuntimeException("Failed to update board");
		}
	}

	// NOTE : 게시글 삭제
	public void deleteBoard(Long boardId, Long userId) {
		int result = boardRepository.deleteBoard(boardId, userId);

		if (result > 0) {
			notificationRepository.save("DELETE", "boards", "게시글이 삭제되었습니다.", "", userId, boardId);
			LoggerUtil.info("Board delete");
		} else {
			LoggerUtil.debug("Board not found");
		}
	}

    // NOTE: 게시글 및 댓글 삭제
    @Transactional
    public void deleteBoardWithComments(Long boardId, Long userId) {
        try {
            int boardDeleteResult = boardRepository.deleteBoard(boardId, userId);

            if (boardDeleteResult > 0) {
                int commentsDeleteResult = commentRepository.deleteAllComment(boardId);
                
                if(commentsDeleteResult >= 0) {
                	notificationRepository.save("DELETE", "boards", "게시글이 삭제되었습니다.", "", userId, boardId);
                	LoggerUtil.info("Board and comments deleted");
                }
                else {
                	LoggerUtil.error("Failed to delete comments for boardId: " + boardId);
                    throw new RuntimeException("댓글 삭제 실패로 인해 트랜잭션을 롤백합니다.");
                }
            } else {
                LoggerUtil.debug("Board not found");
                throw new RuntimeException("게시글 삭제 실패로 인해 트랜잭션을 롤백합니다.");
            }
        } catch (Exception e) {
            LoggerUtil.error("Error occurred during board and comments deletion", e);
            throw new RuntimeException("에러 발생으로 인해 트랜잭션을 롤백합니다.");
        }
    }
    
	public int addViewBoard(Long boardId, Long userId) {
		int result = boardRepository.addViewBoard(boardId, userId);
		if (result > 0) {
			notificationRepository.save("INSERT", "boardview", "조회수가 증가했습니다.", "", userId, boardId);
			LoggerUtil.info("Board view add");
		} else {
			LoggerUtil.debug("Board not found");
		}
		return result;
	}
}
