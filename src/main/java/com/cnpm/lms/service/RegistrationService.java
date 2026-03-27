package com.cnpm.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.lms.domain.AvailableSession;
import com.cnpm.lms.domain.Registration;
import com.cnpm.lms.domain.Student;
import com.cnpm.lms.repository.AvailableSessionRepository;
import com.cnpm.lms.repository.RegistrationRepository;
import com.cnpm.lms.repository.StudentRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrationService {
    @Autowired
    private RegistrationRepository repo;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AvailableSessionRepository availableSessionRepository;

    public Registration save(Registration registration) {
        return repo.save(registration);
    }

    public Registration register(Long studentId, Long availableSessionId) {
        Student student = this.studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        AvailableSession availableSession = this.availableSessionRepository.findById(availableSessionId)
                .orElseThrow(() -> new IllegalArgumentException("AvailableSession not found"));

        if (!availableSession.isOpen()) {
            throw new IllegalStateException("Available session is not open for registration.");
        }

        // BUG FIX (Bug 2): so sanh Long voi long bang .equals() thay vi ==
        // Student.getId() tra ve long (primitive) -> dung studentId.equals() de autobox
        boolean alreadyRegistered = availableSession.getRegistrations()
                .stream()
                .anyMatch(r -> studentId.equals(r.getStudent().getId())
                            && ("pending".equals(r.getStatus()) || "approved".equals(r.getStatus())));
        
        if (alreadyRegistered) {
            throw new IllegalStateException("Student already registered for this session.");
        }

        // Check max capacity
        long currentRegistrations = availableSession.getRegistrations()
                .stream()
                .filter(r -> "pending".equals(r.getStatus()) || "approved".equals(r.getStatus()))
                .count();
                
        if (currentRegistrations >= availableSession.getMaxStudents()) {
            throw new IllegalStateException("Maximum student capacity reached for this session.");
        }

        // BUG FIX (Bug 3): luu Registration TRUOC, sau do moi add vao collection
        // Tranh viec cascade insert registration khi chua co id
        var registration = new Registration();
        registration.setStudent(student);
        registration.setAvailableSession(availableSession);
        registration.setStatus("pending");
        registration.setRegisteredAt(java.time.LocalDateTime.now());

        // Luu registration de co id truoc
        return repo.save(registration);
    }

    public Registration cancelRegistration(Long registrationId) {
        var registration = repo.findById(registrationId).orElse(null);
        if (registration != null) {
            registration.setStatus("canceled");
            return repo.save(registration);
        }
        return null;
    }

    public List<Registration> getRegistrationsByStudentId(Long studentId) {
        return repo.findByStudentId(studentId);
    }

    public List<Registration> getRegistrationsByAvailableSessionId(Long availableSessionId) {
        return repo.findByAvailableSessionId(availableSessionId);
    }

    public Registration getById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
