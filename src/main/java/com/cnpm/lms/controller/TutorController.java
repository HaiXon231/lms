package com.cnpm.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnpm.lms.domain.ConsultationSession;
import com.cnpm.lms.domain.Mapper;
import com.cnpm.lms.domain.Registration;
import com.cnpm.lms.domain.DTO.AvailableSessionDTO;
import com.cnpm.lms.domain.DTO.ConsultationSessionDTO;
import com.cnpm.lms.service.AvailableSessionService;
import com.cnpm.lms.service.ConsultationSessionService;
import com.cnpm.lms.service.FeedBackService;
import com.cnpm.lms.service.ParticipationService;
import com.cnpm.lms.service.RegistrationService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.cnpm.lms.config.CustomUserDetails;
import org.springframework.http.HttpStatus;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/tutor")
public class TutorController {
    @Autowired
    private AvailableSessionService availableSessionService;

    @Autowired
    private ConsultationSessionService consultationSessionService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private ParticipationService participationService;

    @Autowired
    private FeedBackService feedbackService;

    @Autowired
    private Mapper mapper;

    @PostMapping("available-sessions/create")
    public ResponseEntity<?> createAvailableSession(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody AvailableSessionDTO req) {

        req.tutorId = user.getId(); // Override explicitly with JWT identity
        var sessions = availableSessionService.createAvailableSession(req)
                .stream()
                .map(mapper::toAvailableSessionDTO)
                .toList();

        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{tutorId}/available-sessions")
    public ResponseEntity<?> getAvailableSessionsByTutor(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long tutorId) {
        if (!user.getId().equals(tutorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed to view other tutor's sessions");
        }
        var list = availableSessionService.getByTutorId(tutorId)
                .stream()
                .map(mapper::toAvailableSessionDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    @PatchMapping("/available-sessions/{id}/open")
    public ResponseEntity<?> openRegistration(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id) {
        var s = availableSessionService.getAvailableSessionById(id);
        if (s == null || !user.getId().equals(s.getTutor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not your session");
        }
        s = availableSessionService.openAvailableSession(id);
        return ResponseEntity.ok(mapper.toAvailableSessionDTO(s));
    }

    @PostMapping("/consultation-sessions/create")
    public ResponseEntity<?> createConsultationSession(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody ConsultationSessionDTO req) {

        var s = availableSessionService.getAvailableSessionById(req.availableSessionId);
        if (s == null || !user.getId().equals(s.getTutor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not your session");
        }
        ConsultationSession cs = consultationSessionService.createFromAvailableSession(req.availableSessionId, req.room);

        return ResponseEntity.ok(mapper.toConsultationSessionDTO(cs));
    }

    @GetMapping("/{tutorId}/consultation-sessions")
    public ResponseEntity<?> getConsultationSessions(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long tutorId) {
        if (!user.getId().equals(tutorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed to view other tutor's sessions");
        }

        var list = consultationSessionService.getConsultationSessionsByTutorId(tutorId)
                .stream()
                .map(mapper::toConsultationSessionDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("available-sessions/{id}/registrations")
    public ResponseEntity<?> getRegistrations(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id) {
        var s = availableSessionService.getAvailableSessionById(id);
        if (s == null || !user.getId().equals(s.getTutor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not your session");
        }
        var list = registrationService.getRegistrationsByAvailableSessionId(id)
                .stream()
                .map(mapper::toRegistrationDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    @PatchMapping("/registrations/{id}/approve")
    public ResponseEntity<?> approve(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id) {
        Registration r = registrationService.getById(id);
        if (r == null || !user.getId().equals(r.getAvailableSession().getTutor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized");
        }
        Registration reg = consultationSessionService.approvedRegistration(r);
        return ResponseEntity.ok(mapper.toRegistrationDTO(reg));
    }

    @PatchMapping("/registrations/{id}/reject")
    public ResponseEntity<?> reject(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id) {
        Registration r = registrationService.getById(id);
        if (r == null || !user.getId().equals(r.getAvailableSession().getTutor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized");
        }
        Registration reg = consultationSessionService.rejectedRegistration(r);
        return ResponseEntity.ok(mapper.toRegistrationDTO(reg));
    }

    @GetMapping("/consultation-sessions/{id}/participants")
    public ResponseEntity<?> getParticipants(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id) {
        var cs = consultationSessionService.getById(id);
        if (cs == null || !user.getId().equals(cs.getTutor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized");
        }
        var list = participationService.getBySessionId(id)
                .stream()
                .map(mapper::toParticipantDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/consultation-sessions/{id}/feedbacks")
    public ResponseEntity<?> getFeedbacks(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id) {
        var session = consultationSessionService.getById(id);
        if (session == null || !user.getId().equals(session.getTutor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized");
        }

        var result = session.getParticipants()
                .stream()
                .map(p -> feedbackService.findByParticipation(p))
                .filter(f -> f != null && f.getContent() != null && !f.getContent().isBlank())
                .map(mapper::toFeedbackDTO)
                .toList();

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/available-sessions/{id}")
    public ResponseEntity<?> deleteAvailableSession(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id) {
        var s = availableSessionService.getAvailableSessionById(id);
        if (s == null || !user.getId().equals(s.getTutor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not your session or not found");
        }
        try {
            availableSessionService.forceDeleteAvailableSession(id);
            return ResponseEntity.ok("Available session " + id + " deleted. All registrations rejected.");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
