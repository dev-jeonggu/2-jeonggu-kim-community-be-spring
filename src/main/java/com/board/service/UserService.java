package com.board.service;

import com.board.entity.User;
import com.board.repo.UserRepository;
import com.board.repo.admin.NotificationRepository;
import com.board.utils.LoggerUtil;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	@Autowired
	private NotificationRepository notificationRepository;
	
    private final UserRepository userRepository;

    @Autowired
        this.userRepository = userRepository;
    }

    // NOTE : 사용자 조회
    public Optional<User> findUser(String key, String value, Long userId) {
        return userRepository.findUserByKeyAndValue(key, value, userId);
    }

    // NOTE : 사용자 추가
    public Long addUser(User user) {
    	Long result =  0L;
        // NOTE : 이메일 중복 확인
        if (userRepository.isEmailOrNicknameDuplicate("email", user.getEmail(), null)) {
        	LoggerUtil.debug("Email is already in use.");
        	return result;
        }
        
        // NOTE : 닉네임 중복 확인
        if (userRepository.isEmailOrNicknameDuplicate("nickname", user.getNickname(), null)) {
        	LoggerUtil.debug("Nickname is already in use.");
        	return result;
        }
        
        result = userRepository.addUser(user);

        return result;
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
