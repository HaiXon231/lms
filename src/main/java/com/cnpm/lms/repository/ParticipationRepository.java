package com.cnpm.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cnpm.lms.domain.Participation;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findByStudentId(Long studentId);

    List<Participation> findBySessionId(Long sessionId);

    Participation findByStudentIdAndSessionId(long studentId, long sessionId);

}