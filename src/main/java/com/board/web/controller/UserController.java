package com.board.web.controller;

import com.board.entity.User;
import com.board.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 조회
    @GetMapping
    public ResponseEntity<?> findUser(@RequestParam String key, 
                                      @RequestParam String value, 
                                      @RequestParam(required = false) Long userId) {
        Optional<User> user = userService.findUser(key, value, userId);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 사용자 추가
    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user) {
        int rowsAffected = userService.addUser(user);
        if (rowsAffected > 0) {
            return ResponseEntity.ok("User added successfully!");
        } else {
            return ResponseEntity.status(500).body("Failed to add user.");
        }
    }

    // 사용자 정보 업데이트
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody User user) {
        int rowsAffected = userService.updateUser(userId, user);
        if (rowsAffected > 0) {
            return ResponseEntity.ok("User updated successfully!");
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    // 사용자 삭제 및 관련 데이터 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        boolean success = userService.deleteUser(userId);
        if (success) {
            return ResponseEntity.ok("User and related data deleted successfully!");
        } else {
            return ResponseEntity.status(500).body("Failed to delete user.");
        }
    }
}
