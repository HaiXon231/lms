package com.cnpm.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.lms.domain.Tutor;
import com.cnpm.lms.repository.TutorRepository;

@Service
public class TutorService {
    @Autowired
    private TutorRepository tutorRepository;

    public List<Tutor> getAllTutors() {
        return tutorRepository.findAll();
    }

    public Tutor getTutorByEmail(String email) {
        return tutorRepository.findByEmail(email).orElse(null);
    }

    public Tutor getTutorById(long id) {
        return tutorRepository.findById(id).orElse(null);
    }

    public Tutor saveTutor(Tutor tutor) {
        return tutorRepository.save(tutor);
    }

    public void deleteTutorById(long id) {
        tutorRepository.deleteById(id);
    }

}
