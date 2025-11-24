package com.cnpm.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.lms.domain.AvailableSession;
import com.cnpm.lms.domain.ConsultationSession;
import com.cnpm.lms.domain.Participation;
import com.cnpm.lms.domain.Registration;
import com.cnpm.lms.domain.Tutor;
import com.cnpm.lms.repository.AvailableSessionRepository;
import com.cnpm.lms.repository.ConsultationSessionRepository;
import com.cnpm.lms.repository.ParticipationRepository;
import com.cnpm.lms.repository.RegistrationRepository;

@Service
public class ConsultationSessionService {
    @Autowired
    private ConsultationSessionRepository repo;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private AvailableSessionRepository availableSessionRepo;

    @Autowired
    private RegistrationRepository registrationRepo;

    @Autowired
    private ParticipationRepository participationRepo;

    public ConsultationSession getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<ConsultationSession> getConsultationSessionsByTutorId(Long tutorId) {
        return repo.findByTutorId(tutorId);
    }

    public ConsultationSession createFromAvailableSession(Long availableSessionId, String room) {
        ConsultationSession session = new ConsultationSession();
        AvailableSession availableSession = this.availableSessionRepo.findById(availableSessionId).orElseThrow();
        availableSession.setOpen(false);
        availableSessionRepo.save(availableSession);
        Long tutorId = availableSession.getTutor().getId();
        Tutor tutor = this.tutorService.getTutorById(tutorId);
        session.setTutor(tutor);
        session.setSourceAvailableSession(availableSession);
        session.setRoom(room);
        return repo.save(session);
    }

    public void cancel() {
    }

    public Registration approvedRegistration(Registration registration) {
        Participation participation = new Participation();
        participation.setStudent(registration.getStudent());
        AvailableSession availableSession = registration.getAvailableSession();
        ConsultationSession session = repo.findBySourceAvailableSession(availableSession);
        participation.setSession(session);
        List<Participation> participants = session.getParticipants();
        participants.add(participation);
        participationRepo.save(participation);
        session.setParticipants(participants);
        registration.setStatus("approved");
        repo.save(session);
        return this.registrationRepo.save(registration);
    }

    public Registration rejectedRegistration(Registration registration) {
        registration.setStatus("rejected");
        return this.registrationRepo.save(registration);
    }

    public List<ConsultationSession> getByTutorId(Long tutorId) {
        return repo.findByTutorId(tutorId);
    }

}
