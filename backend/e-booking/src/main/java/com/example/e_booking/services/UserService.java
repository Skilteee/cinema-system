package com.example.e_booking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.e_booking.model.User;
import com.example.e_booking.model.UserStatus;
import com.example.e_booking.repositories.UserRepository;
import com.example.utils.PasswordUtil;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public User registerUser(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setStatus(UserStatus.INACTIVE); // 用户初始状态为未激活
        return userRepository.save(user); 
    }


    public boolean validateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        System.out.println(password);
        System.out.println(PasswordUtil.hashPassword(password));
        if (user.isPresent() && user.get().getPassword().equals(PasswordUtil.hashPassword(password))) {
            return true;
        }
        return false;
    }
    
    public boolean getUserByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public Optional<User> getUserInfoByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void updatePassword(String email, String newPassword) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setPassword(PasswordUtil.hashPassword(newPassword)); // 哈希密码
            userRepository.save(user.get());
        } else {
            System.out.println("User not found");
            throw new Exception("User not found");
        }
    }


    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword());
            // existingUser.setPromotion(updatedUser.isPromotion());
            existingUser.setStatus(updatedUser.getStatus()); // 更新用户状态
            return userRepository.save(existingUser);
        }
        return null;
    }

    // 激活用户
    public Optional<User> activateUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent() && !user.get().getStatus().equals(UserStatus.ACTIVE)) {
            user.get().setStatus(UserStatus.ACTIVE);  // 更新激活状态
            userRepository.save(user.get());
        }

        return user;
    }
    // 暂停用户
    public User suspendUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setStatus(UserStatus.SUSPENDED);
            return userRepository.save(user);
        }
        return null;
    }
}

