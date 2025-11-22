package com.cnpm.lms.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.lms.domain.AvailableSession;
import com.cnpm.lms.domain.Registration;
import com.cnpm.lms.domain.Tutor;
import com.cnpm.lms.domain.DTO.AvailableSessionDTO;
import com.cnpm.lms.repository.AvailableSessionRepository;
import com.cnpm.lms.repository.RegistrationRepository;

@Service
public class AvailableSessionService {
    @Autowired
    private AvailableSessionRepository repo;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private RegistrationRepository registrationRepo;

    public List<AvailableSession> getByTutorId(Long tutorId) {
        return repo.findByTutorId(tutorId);
    }

    public List<AvailableSession> getOpenSessionsByTutorId(Long tutorId) {
        return repo.findByTutorIdAndIsOpenTrue(tutorId);
    }

    public AvailableSession saveAvailableSession(AvailableSession session) {
        return repo.save(session);
    }

    public AvailableSession getAvailableSessionById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void addRegistrationToAvailableSession(AvailableSession session, Registration registration) {
        session.getRegistrations().add(registration);
        repo.save(session);
    }

    public void removeRegistrationFromAvailableSession(AvailableSession session, Registration registration) {
        session.getRegistrations().remove(registration);
        repo.save(session);
    }

    public AvailableSession openAvailableSession(Long id) {
        AvailableSession s = repo.findById(id).orElseThrow();
        s.setOpen(true);
        return repo.save(s);
    }

    public void closeAvailableSession(AvailableSession session) {
        session.setOpen(false);
        repo.save(session);
    }

    public List<AvailableSession> createAvailableSession(AvailableSessionDTO req) {

        Tutor tutor = tutorService.getTutorById(req.tutorId);
        List<AvailableSession> result = new ArrayList<>();

        for (var slot : req.slots) {
            AvailableSession s = new AvailableSession();
            s.setTutor(tutor);
            s.setDate(LocalDate.parse(slot.date));
            s.setStartTime(LocalTime.parse(slot.startTime));
            s.setEndTime(LocalTime.parse(slot.endTime));

            s.setName(req.name);
            s.setDescription(req.description);
            s.setType(req.type);
            s.setMinStudents(req.minStudents);
            s.setMaxStudents(req.maxStudents);
            s.setDuration(req.duration);
            s.setOpen(false);

            result.add(repo.save(s));
        }

        return result;
    }

    public boolean forceDeleteAvailableSession(Long id) {
        AvailableSession session = repo.findById(id).orElse(null);

        if (session == null) {
            throw new IllegalArgumentException("Available session not found.");
        }

        if (session.getConsultationSession() != null) {
            throw new IllegalStateException(
                    "Cannot delete. This session has been finalized into a consultation session.");
        }

        List<Registration> regs = session.getRegistrations();
        if (regs != null) {
            for (Registration r : regs) {
                r.setAvailableSession(null);
                r.setStatus("rejected");
                registrationRepo.save(r);
            }
        }
        repo.delete(session);

        return true;
    }

    public void cancel() {
    }

}
