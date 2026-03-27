package com.cnpm.lms.domain;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;
    private String name;
    private String role;
    private String department;
    private String status;
    private long enrollmentYear;
    private double gpa;
    private String avatarUrl;

    @OneToMany(mappedBy = "student")
    private List<Participation> participations;

    @OneToMany(mappedBy = "student")
    private List<Registration> availableSessionRegistrations;

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

    public long getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(long enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    public List<Registration> getAvailableSessionRegistrations() {
        return availableSessionRegistrations;
    }

    public void setAvailableSessionRegistrations(List<Registration> availableSessionRegistrations) {
        this.availableSessionRegistrations = availableSessionRegistrations;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}
