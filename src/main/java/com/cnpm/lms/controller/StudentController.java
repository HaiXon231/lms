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
import jakarta.validation.Valid;

import com.cnpm.lms.domain.Feedback;
import com.cnpm.lms.domain.Mapper;
import com.cnpm.lms.domain.Participation;
import com.cnpm.lms.domain.Registration;
import com.cnpm.lms.service.AvailableSessionService;
import com.cnpm.lms.service.FeedBackService;
import com.cnpm.lms.service.ParticipationService;
import com.cnpm.lms.service.RegistrationService;
import com.cnpm.lms.service.TutorService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.cnpm.lms.config.CustomUserDetails;
import org.springframework.http.HttpStatus;

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
    public ResponseEntity<?> getAllTutors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var tutors = tutorService.getAllTutors(page, size)
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
    public ResponseEntity<?> getAvailableSessions(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var sessions = availableSessionService.getOpenSessionsByTutorId(id, page, size)
                .stream()
                .map(mapper::toAvailableSessionDTO)
                .toList();

        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/available-sessions/{available_sessionsId}/register")
    public ResponseEntity<?> register(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long available_sessionsId) {

        Registration reg = registrationService.register(user.getId(), available_sessionsId);

        return ResponseEntity.ok(mapper.toRegistrationDTO(reg));
    }

    @GetMapping("/registrations")
    public ResponseEntity<?> getRegistrations(@AuthenticationPrincipal CustomUserDetails user) {

        var result = registrationService.getRegistrationsByStudentId(user.getId())
                .stream()
                .map(mapper::toRegistrationDTO)
                .toList();

        return ResponseEntity.ok(result);
    }

    // 6. Hủy đăng ký
    @DeleteMapping("/registrations/{registrationId}")
    public ResponseEntity<?> cancel(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long registrationId) {
        Registration reg = registrationService.getById(registrationId);
        if (reg == null || !user.getId().equals(reg.getStudent().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to cancel this registration");
        }
        return ResponseEntity.ok(registrationService.cancelRegistration(registrationId));
    }

    // 7. Tạo phản hồi cho buổi học
    @PostMapping("/consultation-sessions/{consultation_sessionId}/feedback")
    public ResponseEntity<?> createFeedback(
            @PathVariable Long consultation_sessionId,
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody com.cnpm.lms.domain.DTO.FeedbackRequest req) {

        Participation p = participationService.getByStudentIdAndSessionId(user.getId(), consultation_sessionId);
        if (p == null) {
            return ResponseEntity.badRequest().body("You did not participate in this session.");
        }

        Feedback fb = feedbackService.create(p, req.content, req.rating);

        return ResponseEntity.ok(mapper.toFeedbackDTO(fb));
    }

    @GetMapping("/consultation-sessions")
    public ResponseEntity<?> getStudentConsultationSessions(@AuthenticationPrincipal CustomUserDetails user) {
        var participations = participationService.getByStudentId(user.getId());
        var sessions = participations.stream()
                .map(p -> p.getSession())
                .map(s -> mapper.toConsultationSessionDTO(s))
                .toList();

        return ResponseEntity.ok(sessions);

    }
}
