package com.cnpm.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cnpm.lms.domain.Feedback;
import com.cnpm.lms.domain.Participation;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback findByParticipation(Participation participation);
}
