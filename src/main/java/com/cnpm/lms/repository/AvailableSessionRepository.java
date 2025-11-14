package com.cnpm.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cnpm.lms.domain.AvailableSession;

@Repository
public interface AvailableSessionRepository extends JpaRepository<AvailableSession, Long> {
    List<AvailableSession> findByTutorId(Long tutorId);

    List<AvailableSession> findByTutorIdAndIsOpenTrue(Long tutorId);
}
