package com.board.service;

import com.board.entity.User;
import com.board.repo.UserRepository;
import com.board.repo.admin.NotificationRepository;
import com.board.utils.LoggerUtil;
import com.board.utils.PasswordUtil;
import com.board.utils.ResponseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	@Autowired
	private NotificationRepository notificationRepository;
	
    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordUtil passwordUtil) {
        this.userRepository = userRepository;
        this.passwordUtil = passwordUtil;
    }

    // NOTE : 사용자 조회
    public Map<String, Object> findUser(String key, String value, Long userId) {
        List<User> user = userRepository.findUserByKeyAndValue(key, value, userId);
        Map<String, Object> innerResponse = new HashMap<>();

        if (user.isEmpty()) {
        	innerResponse = ResponseUtil.successResponse(null);
        } else {
        	innerResponse = ResponseUtil.errorResponse(null);
        }

        // 외부 맵으로 감싸기
        Map<String, Object> outerResponse = new HashMap<>();
        outerResponse.put("data", innerResponse);

        return outerResponse;
    }

    // NOTE : 사용자 추가
    public boolean addUser(User user) {
        // 이메일 중복 확인
        if (userRepository.isEmailOrNicknameDuplicate("email", user.getEmail(), null)) {
            LoggerUtil.debug("Email is already in use.");
            return false;
        }

        // 닉네임 중복 확인
        if (userRepository.isEmailOrNicknameDuplicate("nickname", user.getNickname(), null)) {
            LoggerUtil.debug("Nickname is already in use.");
            return false;
        }

        // 사용자 추가 로직
        String decodedPassword = passwordUtil.decodeBase64(user.getPassword());
        String encodedPassword = passwordUtil.encodePassword(decodedPassword);
        user.setPassword(encodedPassword);
        Long result = userRepository.addUser(user);
        
        if (result > 0) {
            notificationRepository.save("INSERT", "users", "사용자가 추가되었습니다", result);
            return true;
        } else {
            return false;
        }
    }
    // NOTE : 사용자 정보 업데이트
    public int updateUser(Long userId, User user) {
    	int result = 0;
    	// NOTE : 닉네임 중복 확인
        if (userRepository.isEmailOrNicknameDuplicate("nickname", user.getNickname(), null)) {
        	LoggerUtil.debug("Nickname is already in use.");
        	return result;
        }
        
        result = userRepository.updateUser(userId, user);
        if(result > 0) {
        	notificationRepository.save("UPDATE", "users", "사용자가 수정되었습니다", userId);
        }
        return result;
    }

    // NOTE : 사용자 삭제 및 관련 데이터 삭제
    @Transactional
    public boolean deleteUser(Long userId) {
        try {
            // NOTE : 댓글 삭제
            userRepository.deleteCommentsByUserId(userId);

            // NOTE : 게시글 삭제
            userRepository.deleteBoardsByUserId(userId);

            // NOTE : 사용자 삭제
            userRepository.deleteUser(userId);

        	notificationRepository.save("DELETE", "users", "사용자가 삭제되었습니다", userId);
            
        	return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
