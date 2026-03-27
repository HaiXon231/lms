package com.cnpm.lms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cnpm.lms.repository.StudentRepository;
import com.cnpm.lms.repository.TutorRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TutorRepository tutorRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Hash student passwords if they aren't hashed yet
        studentRepo.findAll().forEach(s -> {
            if (s.getPassword() != null && !s.getPassword().startsWith("$2a$")) {
                s.setPassword(passwordEncoder.encode(s.getPassword()));
                studentRepo.save(s);
            }
        });

        // Hash tutor passwords if they aren't hashed yet
        tutorRepo.findAll().forEach(t -> {
            if (t.getPassword() != null && !t.getPassword().startsWith("$2a$")) {
                t.setPassword(passwordEncoder.encode(t.getPassword()));
                tutorRepo.save(t);
            }
        });
    }
}
