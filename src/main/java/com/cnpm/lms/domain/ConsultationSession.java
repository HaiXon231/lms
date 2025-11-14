package com.cnpm.lms.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class ConsultationSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String room; // phòng học

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    // Quan hệ 1-1 với lịch khảo sát
    @OneToOne
    @JoinColumn(name = "available_session_id")
    private AvailableSession sourceAvailableSession;

    @OneToMany(mappedBy = "session")
    private List<Participation> participants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public AvailableSession getSourceAvailableSession() {
        return sourceAvailableSession;
    }

    public void setSourceAvailableSession(AvailableSession sourceAvailableSession) {
        this.sourceAvailableSession = sourceAvailableSession;
    }

    public List<Participation> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participation> participants) {
        this.participants = participants;
    }

}
