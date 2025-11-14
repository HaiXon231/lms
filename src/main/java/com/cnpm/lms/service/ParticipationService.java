package com.cnpm.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cnpm.lms.domain.Participation;
import com.cnpm.lms.repository.ParticipationRepository;

@Service
public class ParticipationService {
    @Autowired
    private ParticipationRepository repo;

    public List<Participation> getByStudentId(Long studentId) {
        return repo.findByStudentId(studentId);
    }

    public List<Participation> getBySessionId(Long sessionId) {
        return repo.findBySessionId(sessionId);
    }

    public Participation save(Participation participation) {
        return repo.save(participation);
    }

    public Participation getByStudentIdAndSessionId(long studentId, long sessionId) {
        return repo.findByStudentIdAndSessionId(studentId, sessionId);
    }

}
