package com.cnpm.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnpm.lms.domain.Tutor;
import com.cnpm.lms.repository.TutorRepository;

import org.springframework.data.domain.PageRequest;

@Service
public class TutorService {
    @Autowired
    private TutorRepository tutorRepository;

    public List<Tutor> getAllTutors(int page, int size) {
        return tutorRepository.findAll(PageRequest.of(page, size)).getContent();
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
