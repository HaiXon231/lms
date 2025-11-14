package com.cnpm.lms.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;
    private String name;
    private String role;
    private String department;
    private String status;
    private long experienceYears;
    private String educationLevel;

    @OneToMany(mappedBy = "tutor")
    private List<AvailableSession> availableSessions;

    @OneToMany(mappedBy = "tutor")
    private List<ConsultationSession> consultationSessions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(long experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public List<AvailableSession> getAvailableSessions() {
        return availableSessions;
    }

    public void setAvailableSessions(List<AvailableSession> availableSessions) {
        this.availableSessions = availableSessions;
    }

    public List<ConsultationSession> getConsultationSessions() {
        return consultationSessions;
    }

    public void setConsultationSessions(List<ConsultationSession> consultationSessions) {
        this.consultationSessions = consultationSessions;
    }

}
