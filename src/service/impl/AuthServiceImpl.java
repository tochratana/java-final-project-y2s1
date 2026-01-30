package service.impl;

import dto.UserLoginDTO;
import dto.UserRegistrationDTO;
import model.User;
import repository.UserRepository;
import service.AuthService;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

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

            // Email validation
            if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }

            if (!isValidEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Invalid email format");
            }

            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }

            // Check if email already exists (optional but recommended)
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
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

    // Email validation method
    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    private String hashPassword(String password) {
        return "hashed_" + password;
    }

    private boolean verifyPassword(String plain, String hashed) {
        return hashed.equals("hashed_" + plain);
    }
}