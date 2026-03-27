package com.cnpm.lms.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnpm.lms.repository.AvailableSessionRepository;
import com.cnpm.lms.repository.RegistrationRepository;
import com.cnpm.lms.repository.StudentRepository;
import com.cnpm.lms.repository.TutorRepository;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TutorRepository tutorRepo;

    @Autowired
    private AvailableSessionRepository availableSessionRepo;

    @Autowired
    private RegistrationRepository registrationRepo;

    @GetMapping("/stats")
    public ResponseEntity<?> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", studentRepo.count());
        stats.put("totalTutors", tutorRepo.count());
        stats.put("totalAvailableSessions", availableSessionRepo.count());
        stats.put("totalRegistrations", registrationRepo.count());
        
        // Count approved vs rejected
        long approved = registrationRepo.findAll().stream().filter(r -> "approved".equals(r.getStatus())).count();
        long rejected = registrationRepo.findAll().stream().filter(r -> "rejected".equals(r.getStatus())).count();
        
        stats.put("approvedRegistrations", approved);
        stats.put("rejectedRegistrations", rejected);
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(studentRepo.findAll());
    }
    
    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        studentRepo.deleteById(id);
        return ResponseEntity.ok("Deleted student " + id);
    }
    
    @GetMapping("/tutors")
    public ResponseEntity<?> getAllTutors() {
        return ResponseEntity.ok(tutorRepo.findAll());
    }
    
    @DeleteMapping("/tutors/{id}")
    public ResponseEntity<?> deleteTutor(@PathVariable Long id) {
        tutorRepo.deleteById(id);
        return ResponseEntity.ok("Deleted tutor " + id);
    }
}
