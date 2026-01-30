package service.impl;

import dto.UserLoginDTO;
import dto.UserRegistrationDTO;
import model.User;
import repository.UserRepository;
import service.AuthService;
import java.time.LocalDateTime;

public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(UserRegistrationDTO dto) {
        try {
            // Validate input
            if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("Username is required");
            }

            if (dto.getPassword() == null || dto.getPassword().length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters");
            }

            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }

            String hashedPassword = hashPassword(dto.getPassword());

            User user = User.builder()
                    .username(dto.getUsername())
                    .password(hashedPassword)
                    .fullName(dto.getFullName())
                    .email(dto.getEmail())
                    .phone(dto.getPhone())
                    .role("USER")
                    .status("ACTIVE")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    @Override
    public User login(UserLoginDTO dto) {
        try {
            User user = userRepository.findByUsername(dto.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

            if (!verifyPassword(dto.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Invalid username or password");
            }

            if (!"ACTIVE".equals(user.getStatus())) {
                throw new IllegalArgumentException("Account is not active");
            }

            return user;
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }

    private String hashPassword(String password) {
        return "hashed_" + password;
    }

    private boolean verifyPassword(String plain, String hashed) {
        return hashed.equals("hashed_" + plain);
    }
}