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

import com.cnpm.lms.domain.AvailableSession;
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
            @RequestBody AvailableSessionDTO req) {

        var sessions = availableSessionService.createAvailableSession(req)
                .stream()
                .map(mapper::toAvailableSessionDTO)
                .toList();

        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{tutorId}/available-sessions")
    public ResponseEntity<?> getAvailableSessionsByTutor(@PathVariable Long tutorId) {
        var list = availableSessionService.getByTutorId(tutorId)
                .stream()
                .map(mapper::toAvailableSessionDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    @PatchMapping("/available-sessions/{id}/open")
    public ResponseEntity<?> openRegistration(@PathVariable Long id) {
        var s = availableSessionService.openAvailableSession(id);
        return ResponseEntity.ok(mapper.toAvailableSessionDTO(s));
    }

    @PostMapping("/consultation-sessions/create")
    public ResponseEntity<?> createConsultationSession(
            @RequestBody ConsultationSessionDTO req) {

        ConsultationSession s = consultationSessionService.createFromAvailableSession(req.availableSessionId, req.room);

        return ResponseEntity.ok(mapper.toConsultationSessionDTO(s));
    }

    @GetMapping("/{tutorId}/consultation-sessions")
    public ResponseEntity<?> getConsultationSessions(@PathVariable Long tutorId) {

        var list = consultationSessionService.getConsultationSessionsByTutorId(tutorId)
                .stream()
                .map(mapper::toConsultationSessionDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("available-sessions/{id}/registrations")
    public ResponseEntity<?> getRegistrations(@PathVariable Long id) {
        var list = registrationService.getRegistrationsByAvailableSessionId(id)
                .stream()
                .map(mapper::toRegistrationDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    @PatchMapping("/registrations/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        Registration r = registrationService.getById(id);
        Registration reg = consultationSessionService.approvedRegistration(r);
        return ResponseEntity.ok(mapper.toRegistrationDTO(reg));
    }

    @PatchMapping("/registrations/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        Registration r = registrationService.getById(id);
        Registration reg = consultationSessionService.rejectedRegistration(r);
        return ResponseEntity.ok(mapper.toRegistrationDTO(reg));
    }

    @GetMapping("/consultation-sessions/{id}/participants")
    public ResponseEntity<?> getParticipants(@PathVariable Long id) {
        var list = participationService.getBySessionId(id)
                .stream()
                .map(mapper::toParticipantDTO)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/consultation-sessions/{id}/feedbacks")
    public ResponseEntity<?> getFeedbacks(@PathVariable Long id) {

        var session = consultationSessionService.getById(id);

        var result = session.getParticipants()
                .stream()
                .map(p -> feedbackService.findByParticipation(p))
                .filter(f -> f != null && f.getContent() != null && !f.getContent().isBlank())
                .map(mapper::toFeedbackDTO)
                .toList();

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/available-sessions/{id}")
    public ResponseEntity<?> deleteAvailableSession(@PathVariable Long id) {
        try {
            availableSessionService.forceDeleteAvailableSession(id);
            return ResponseEntity.ok("Available session " + id + " deleted. All registrations rejected.");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
