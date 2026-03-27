package com.cnpm.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.lms.domain.Feedback;
import com.cnpm.lms.domain.Participation;
import com.cnpm.lms.repository.FeedbackRepository;

@Service
public class FeedBackService {
    @Autowired
    private FeedbackRepository repo;

    public Feedback create(Participation participation, String content, Integer rating) {
        Feedback feedback = new Feedback();
        feedback.setParticipation(participation);
        feedback.setContent(content);
        feedback.setRating(rating);
        return repo.save(feedback);
    }

    public Feedback findByParticipation(Participation participation) {
        return repo.findByParticipation(participation);
    }
}
