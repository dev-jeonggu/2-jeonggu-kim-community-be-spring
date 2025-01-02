package com.board.service;

import com.board.entity.User;
import com.board.repo.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 조회
    public Optional<User> findUser(String key, String value, Long userId) {
        return userRepository.findUserByKeyAndValue(key, value, userId);
    }

    // 사용자 추가
    public int addUser(User user) {
        return userRepository.addUser(user);
    }

    // 사용자 정보 업데이트
    public int updateUser(Long userId, User user) {
        return userRepository.updateUser(userId, user);
    }

    // 사용자 삭제 및 관련 데이터 삭제
    @Transactional
    public boolean deleteUser(Long userId) {
        try {
            // 댓글 삭제
            userRepository.deleteCommentsByUserId(userId);

            // 게시글 삭제
            userRepository.deleteBoardsByUserId(userId);

            // 사용자 삭제
            userRepository.deleteUser(userId);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
