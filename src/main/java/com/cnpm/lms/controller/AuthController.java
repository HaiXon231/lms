package com.cnpm.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnpm.lms.domain.LoginRequest;
import com.cnpm.lms.domain.LoginResponse;
import com.cnpm.lms.repository.StudentRepository;
import com.cnpm.lms.repository.TutorRepository;
import com.cnpm.lms.config.JwtUtils;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TutorRepository tutorRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {

        // Check student
        var student = studentRepo.findByEmail(req.email).orElse(null);
        if (student != null && passwordEncoder.matches(req.password, student.getPassword())) {
            String token = jwtUtils.generateJwtToken(student.getEmail(), "student", student.getId());
            LoginResponse response = new LoginResponse(student.getId(), student.getName(), "student", student.getAvatarUrl());
            // Set token in response (assuming LoginResponse needs a token field, wait, does LoginResponse have it?)
            // We'll return a custom map or we need to update LoginResponse domain.
            return ResponseEntity.ok(new AuthResponse(token, response));
        }

        // Check tutor
        var tutor = tutorRepo.findByEmail(req.email).orElse(null);
        if (tutor != null && passwordEncoder.matches(req.password, tutor.getPassword())) {
            String token = jwtUtils.generateJwtToken(tutor.getEmail(), "tutor", tutor.getId());
            LoginResponse response = new LoginResponse(tutor.getId(), tutor.getName(), "tutor", tutor.getAvatarUrl());
            return ResponseEntity.ok(new AuthResponse(token, response));
        }

        // Check admin
        if ("admin@lms".equals(req.email) && "admin123".equals(req.password)) {
            String token = jwtUtils.generateJwtToken("admin@lms", "admin", 0L);
            LoginResponse response = new LoginResponse(0L, "Administrator", "admin", "https://i.pravatar.cc/150?u=admin@lms");
            return ResponseEntity.ok(new AuthResponse(token, response));
        }

        return ResponseEntity.badRequest().body("Invalid email or password");
    }

    // Helper class to return JWT alongside standard response
    public static class AuthResponse {
        public String token;
        public LoginResponse user;

        public AuthResponse(String token, LoginResponse user) {
            this.token = token;
            this.user = user;
        }
    }
}
