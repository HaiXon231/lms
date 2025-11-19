package com.cnpm.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnpm.lms.domain.Feedback;
import com.cnpm.lms.domain.Mapper;
import com.cnpm.lms.domain.Participation;
import com.cnpm.lms.domain.Registration;
import com.cnpm.lms.service.AvailableSessionService;
import com.cnpm.lms.service.FeedBackService;
import com.cnpm.lms.service.ParticipationService;
import com.cnpm.lms.service.RegistrationService;
import com.cnpm.lms.service.TutorService;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    @Autowired
    private TutorService tutorService;

    @Autowired
    private AvailableSessionService availableSessionService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private FeedBackService feedbackService;

    @Autowired
    private ParticipationService participationService;

    @Autowired
    private Mapper mapper;

    @GetMapping("/tutors")
    public ResponseEntity<?> getAllTutors() {
        var tutors = tutorService.getAllTutors()
                .stream()
                .map(mapper::toTutorListDTO)
                .toList();

        return ResponseEntity.ok(tutors);
    }

    @GetMapping("/tutors/{id}")
    public ResponseEntity<?> getTutorById(@PathVariable Long id) {
        return ResponseEntity.ok(
                mapper.toTutorDetailDTO(tutorService.getTutorById(id)));
    }

    @GetMapping("/tutors/{id}/available-sessions")
    public ResponseEntity<?> getAvailableSessions(@PathVariable Long id) {
        var sessions = availableSessionService.getOpenSessionsByTutorId(id)
                .stream()
                .map(mapper::toAvailableSessionDTO)
                .toList();

        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/available-sessions/{available_sessionsId}/register")
    public ResponseEntity<?> register(
            @RequestParam Long studentId,
            @PathVariable Long available_sessionsId) {

        Registration reg = registrationService.register(studentId, available_sessionsId);

        return ResponseEntity.ok(mapper.toRegistrationDTO(reg));
    }

    @GetMapping("/registrations")
    public ResponseEntity<?> getRegistrations(@RequestParam Long studentId) {

        var result = registrationService.getRegistrationsByStudentId(studentId)
                .stream()
                .map(mapper::toRegistrationDTO)
                .toList();

        return ResponseEntity.ok(result);
    }

    // 6. Hủy đăng ký
    @DeleteMapping("/registrations/{registrationId}")
    public ResponseEntity<?> cancel(@PathVariable Long registrationId) {
        return ResponseEntity.ok(registrationService.cancelRegistration(registrationId));
    }

    // 7. Tạo phản hồi cho buổi học
    @PostMapping("/consultation-sessions/{consultation_sessionId}/feedback")
    public ResponseEntity<?> createFeedback(
            @PathVariable Long consultation_sessionId,
            @RequestParam Long studentId,
            @RequestBody String content) {

        Participation p = participationService.getByStudentIdAndSessionId(studentId, consultation_sessionId);

        Feedback fb = feedbackService.create(p, content);

        return ResponseEntity.ok(mapper.toFeedbackDTO(fb));
    }

    @GetMapping("/consultation-sessions")
    public ResponseEntity<?> getStudentConsultationSessions(@RequestParam Long studentId) {
        var participations = participationService.getByStudentId(studentId);
        var sessions = participations.stream()
                .map(p -> p.getSession())
                .map(s -> mapper.toConsultationSessionDTO(s))
                .toList();

        return ResponseEntity.ok(sessions);

    }
}
