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

@Service
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
        var registration = new Registration();
        Student student = this.studentRepository.findById(studentId).orElse(null);
        AvailableSession availableSession = this.availableSessionRepository.findById(availableSessionId).orElse(null);

        registration.setStudent(student);
        registration.setAvailableSession(availableSession);
        registration.setStatus("pending");

        availableSession.getRegistrations().add(registration);
        availableSessionRepository.save(availableSession);

        student.getAvailableSessionRegistrations().add(registration);
        studentRepository.save(student);

        return repo.save(registration);
    }

    public Registration cancelRegistration(Long registrationId) {
        var registration = repo.findById(registrationId).orElse(null);
        if (registration != null) {
            registration.setStatus("canceled");
            registration.getAvailableSession().getRegistrations().remove(registration);
            availableSessionRepository.save(registration.getAvailableSession());
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
