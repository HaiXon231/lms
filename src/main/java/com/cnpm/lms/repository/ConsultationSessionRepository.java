package com.cnpm.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cnpm.lms.domain.AvailableSession;
import com.cnpm.lms.domain.ConsultationSession;

@Repository
public interface ConsultationSessionRepository extends JpaRepository<ConsultationSession, Long> {
    List<ConsultationSession> findByTutorId(Long tutorId);

    ConsultationSession findBySourceAvailableSession(AvailableSession availableSession);
}
