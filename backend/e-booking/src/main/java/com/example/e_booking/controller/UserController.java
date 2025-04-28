package com.example.e_booking.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.e_booking.services.EmailService;
import com.example.e_booking.services.UserService;
import com.example.utils.PasswordUtil;
import com.example.e_booking.model.Role;
import com.example.e_booking.model.User;
import com.example.e_booking.model.UserStatus;
import com.example.e_booking.repositories.UserRepository;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    // 获取所有用户
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 添加用户
    @PostMapping("/add")
    public ResponseEntity<User> addUserByAdmin(@RequestBody User user) {
        user.setPassword( PasswordUtil.hashPassword("123456"));
       
        user.setStatus(UserStatus.ACTIVE);
        System.out.println("Adding user by admin...");
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        User savedUser = userRepository.save(user);
        System.out.println("User added successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }


    // 更新用户
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    // 普通用户注册接口
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        System.out.println("Registering regular user...");

        // 检查用户名是否已被使用
        if (userService.getUserByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }

        // 检查电子邮件是否已被使用
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use.");
        }

        try {
            // 正常注册流程
            userService.registerUser(user);

            // 发送确认电子邮件（异步）
            System.out.println("Sending confirmation email...");
            String subject = "Registration Confirmation";
            String body = "Dear " + user.getUsername() + ",\n\nThank you for registering! Please confirm your email by clicking the following link:\n\n"
                          + "http://localhost:8080/confirm-email?token=" + generateToken(user) + "&username=" + user.getUsername()
                          + "\n\nBest regards,\n e-booking Company";

            // 异步调用发送邮件的方法
            emailService.sendConfirmationEmail(user.getEmail(), subject, body);  

            System.out.println("Email processing started.");
            return ResponseEntity.ok("User registered successfully. Please check your email for confirmation.");  // 立即返回成功消息
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user.");
        }
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<String> confirmEmail(@RequestParam("token") String token) {
        // 验证 token 是否有效，并激活用户账号
        boolean isValidToken = validateToken(token);
    
        if (isValidToken) {
            return ResponseEntity.ok("Email confirmed successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid confirmation token.");
        }
    }

    // 激活用户
    @GetMapping("/confirm")
    public ResponseEntity<?> confirmUser(@RequestParam("email") String email) {
        Optional<User> user = userService.activateUser(email);
        if (user.isPresent()) {
            return ResponseEntity.ok("User activated.");
        } else {
            return ResponseEntity.badRequest().body("User not found or already active.");
        }
    }

    // 暂停用户（管理员操作）
    @PutMapping("/suspend/{id}")
    public ResponseEntity<?> suspendUser(@PathVariable Long id) {
        User user = userService.suspendUser(id);
        if (user != null) {
            return ResponseEntity.ok("User suspended.");
        } else {
            return ResponseEntity.badRequest().body("User not found.");
        }
    }

    // 查看用户状态
    @GetMapping("/{id}/status")
    public ResponseEntity<?> getUserStatus(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok("User status: " + user.getStatus());
        } else {
            return ResponseEntity.badRequest().body("User not found.");
        }
    }

    private String generateToken(User user) {
        // 实际中你可以根据用户信息生成一个唯一的 token，比如使用 JWT 或随机生成一个 UUID
        return UUID.randomUUID().toString();
    }
    
    private boolean validateToken(String token) {
        return true; 
    }
    

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginUser) {
        Optional<User> userOptional = userRepository.findByUsername(loginUser.getUsername());

        if (userOptional.isPresent() && userService.validateUser(loginUser.getUsername(), loginUser.getPassword())) {
            User user = userOptional.get();

            // 生成一个模拟 Token（在实际应用中应使用 JWT 或其他方式）
            String token = UUID.randomUUID().toString();

            // 返回 token 和 role 信息
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRole().name()); // 返回角色名称
            response.put("id", user.getId());
            return ResponseEntity.ok(response);
        }

        // 用户名或密码无效时，返回 401 Unauthorized
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {
        String newPassword = requestBody.get("newPassword");
        String email = requestBody.get("email");

        System.out.println("Email: " + email);
        System.out.println("New Password: " + newPassword);

        
        try {
            userService.updatePassword(email, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password reset successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Password reset failed"));
        }
    }




}
