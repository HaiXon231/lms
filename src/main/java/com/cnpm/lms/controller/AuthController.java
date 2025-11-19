package com.cnpm.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnpm.lms.domain.LoginRequest;
import com.cnpm.lms.domain.LoginResponse;
import com.cnpm.lms.repository.StudentRepository;
import com.cnpm.lms.repository.TutorRepository;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TutorRepository tutorRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        // Check student
        var student = studentRepo.findByEmail(req.email).orElse(null);
        if (student != null && student.getPassword().equals(req.password)) {
            return ResponseEntity.ok(new LoginResponse(student.getId(), student.getName(), "student"));
        }

        // Check tutor
        var tutor = tutorRepo.findByEmail(req.email).orElse(null);
        if (tutor != null && tutor.getPassword().equals(req.password)) {
            return ResponseEntity.ok(new LoginResponse(tutor.getId(), tutor.getName(), "tutor"));
        }

        return ResponseEntity.badRequest().body("Invalid email or password");
    }
}
