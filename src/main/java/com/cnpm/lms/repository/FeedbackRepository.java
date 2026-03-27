package com.cnpm.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cnpm.lms.domain.Feedback;
import com.cnpm.lms.domain.Participation;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback findByParticipation(Participation participation);

    @Query("SELECT AVG(f.rating) FROM Feedback f JOIN f.participation p JOIN p.session s JOIN s.sourceAvailableSession a JOIN a.tutor t WHERE t.id = :tutorId AND f.rating IS NOT NULL")
    Double getAverageRatingForTutor(@Param("tutorId") Long tutorId);
}
