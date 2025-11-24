package com.cnpm.lms.domain;

import org.springframework.stereotype.Component;

import com.cnpm.lms.domain.DTO.ConsultationSessionDTO;
import com.cnpm.lms.domain.DTO.FeedbackDTO;
import com.cnpm.lms.domain.DTO.ParticipantDTO;
import com.cnpm.lms.domain.DTO.RegistrationDTO;
import com.cnpm.lms.domain.DTO.SingleAvailableSessionDTO;
import com.cnpm.lms.domain.DTO.TutorDTO;
import com.cnpm.lms.domain.DTO.TutorDetailDTO;
import com.cnpm.lms.domain.DTO.TutorListDTO;

@Component
public class Mapper {

    public TutorDTO toTutorDTO(Tutor t) {
        TutorDTO dto = new TutorDTO();
        dto.id = t.getId();
        dto.name = t.getName();
        dto.email = t.getEmail();
        dto.department = t.getDepartment();
        dto.status = t.getStatus();
        dto.experienceYears = (int) t.getExperienceYears();
        dto.educationLevel = t.getEducationLevel();
        return dto;
    }

    public SingleAvailableSessionDTO toAvailableSessionDTO(AvailableSession s) {
        SingleAvailableSessionDTO dto = new SingleAvailableSessionDTO();

        dto.id = s.getId();
        dto.date = s.getDate().toString();
        dto.startTime = s.getStartTime().toString();
        dto.endTime = s.getEndTime().toString();

        dto.name = s.getName();
        dto.description = s.getDescription();
        dto.type = s.getType();

        dto.minStudents = s.getMinStudents();
        dto.maxStudents = s.getMaxStudents();
        dto.duration = s.getDuration();
        dto.open = s.isOpen();

        dto.tutorId = s.getTutor().getId();
        dto.tutorName = s.getTutor().getName();

        return dto;
    }

    public RegistrationDTO toRegistrationDTO(Registration reg) {
        RegistrationDTO dto = new RegistrationDTO();

        dto.id = reg.getId();
        dto.status = reg.getStatus();

        // student
        dto.studentId = reg.getStudent().getId();
        dto.studentName = reg.getStudent().getName();

        // available session
        if (reg.getAvailableSession() != null) {
            var a = reg.getAvailableSession();
            dto.availableSessionId = a.getId();
            dto.sessionName = a.getName();
            dto.date = a.getDate();
            dto.startTime = a.getStartTime();
            dto.endTime = a.getEndTime();
            dto.description = a.getDescription();
            dto.type = a.getType();
            dto.minStudents = a.getMinStudents();
            dto.maxStudents = a.getMaxStudents();
            dto.duration = a.getDuration();

            // tutor (clone)
            dto.tutorId = a.getTutor().getId();
            dto.tutorName = a.getTutor().getName();
        }

        return dto;
    }

    public ConsultationSessionDTO toConsultationSessionDTO(ConsultationSession cs) {
        ConsultationSessionDTO dto = new ConsultationSessionDTO();

        dto.id = cs.getId();
        dto.room = cs.getRoom();

        var a = cs.getSourceAvailableSession();

        dto.availableSessionId = a.getId();
        dto.date = a.getDate();
        dto.startTime = a.getStartTime();
        dto.endTime = a.getEndTime();
        dto.name = a.getName();
        dto.description = a.getDescription();
        dto.type = a.getType();
        dto.minStudents = a.getMinStudents();
        dto.maxStudents = a.getMaxStudents();
        dto.duration = a.getDuration();

        dto.tutorId = cs.getTutor().getId();
        dto.tutorName = cs.getTutor().getName();

        return dto;
    }

    public ParticipantDTO toParticipantDTO(Participation p) {
        ParticipantDTO dto = new ParticipantDTO();
        dto.id = p.getId();

        dto.studentId = p.getStudent().getId();
        dto.studentName = p.getStudent().getName();

        dto.sessionId = p.getSession().getId();
        dto.room = p.getSession().getRoom();
        return dto;
    }

    public FeedbackDTO toFeedbackDTO(Feedback fb) {
        FeedbackDTO dto = new FeedbackDTO();

        dto.id = fb.getId();
        dto.content = fb.getContent();

        var p = fb.getParticipation();

        // student info
        dto.studentId = p.getStudent().getId();
        dto.studentName = p.getStudent().getName();

        // session info
        dto.consultationSessionId = p.getSession().getId();
        dto.sessionName = p.getSession().getSourceAvailableSession().getName();

        return dto;
    }

    public TutorListDTO toTutorListDTO(Tutor t) {
        TutorListDTO dto = new TutorListDTO();
        dto.id = t.getId();
        dto.name = t.getName();
        dto.department = t.getDepartment();
        dto.educationLevel = t.getEducationLevel();
        dto.experienceYears = (int) t.getExperienceYears();
        return dto;
    }

    public TutorDetailDTO toTutorDetailDTO(Tutor t) {
        TutorDetailDTO dto = new TutorDetailDTO();
        dto.id = t.getId();
        dto.name = t.getName();
        dto.email = t.getEmail();
        dto.department = t.getDepartment();
        dto.status = t.getStatus();
        dto.educationLevel = t.getEducationLevel();
        dto.experienceYears = (int) t.getExperienceYears();
        return dto;
    }

}
