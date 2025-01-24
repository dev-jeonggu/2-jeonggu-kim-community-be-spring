package com.board.web.controller;

import com.board.dto.BoardResponse;
import com.board.entity.User;
import com.board.service.UserService;
import com.board.utils.JwtUtil;
import com.board.utils.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
//
//    @GetMapping
//    public ResponseEntity<?> findUser(@RequestParam String key, 
//                                      @RequestParam String value, 
//                                      @RequestParam(required = false) Long userId) {
//        Optional<User> user = userService.findUser(key, value, userId);
//        return user.map(ResponseEntity::ok)
//                   .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//    
    // NOTE : 사용자 조회
    @GetMapping("/check")
    public ResponseEntity<?> findUser(@RequestParam String key, @RequestParam String value) {
    	Map<String, Object> map = userService.findUser(key, value, null);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
    
    @GetMapping
    public ResponseEntity<?> findUser() {
    	Long userId = JwtUtil.getUserIdFromSecurityContext();
    	Map<String, Object> map = userService.findUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
    
    @PostMapping("/check")
    public ResponseEntity<?> checkUser(HttpServletRequest request, @RequestBody Map<String, String> requestBody) {
    	Long userId = JwtUtil.getUserIdFromSecurityContext();
    	String key = requestBody.get("key");
    	String value = requestBody.get("value");
    	
    	Map<String, Object> map = userService.findUser(key, value, userId);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
    
    // NOTE : 사용자 추가
    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
    	boolean result = userService.addUser(user);
        if (result) {
            return ResponseEntity.ok(ResponseUtil.successResponse("User added successfully!"));
        } else {
            return ResponseEntity.status(500).body(ResponseUtil.errorResponse(null));
        }
    }

    // NOTE : 사용자 정보 업데이트
    @PatchMapping
    public ResponseEntity<?> updateUser(HttpServletRequest request, @RequestBody User user) {
    	Long userId = JwtUtil.getUserIdFromSecurityContext();

    	Map<String,Object> rowsAffected = userService.updateUser(userId, user);
        if (rowsAffected!= null) {
            return ResponseEntity.ok(rowsAffected);
        } else {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    // NOTE : 사용자 삭제 및 관련 데이터 삭제
    @DeleteMapping
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
    	Long userId = JwtUtil.getUserIdFromSecurityContext();
        boolean success = userService.deleteUser(userId);
        if (success) {
            return ResponseEntity.ok("User and related data deleted successfully!");
        } else {
            return ResponseEntity.status(500).body("Failed to delete user.");
        }
    }
}
