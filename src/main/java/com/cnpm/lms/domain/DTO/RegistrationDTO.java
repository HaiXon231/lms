package com.cnpm.lms.domain.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class RegistrationDTO {

    // Registration info
    public Long id;
    public String status;

    // Student info
    public Long studentId;
    public String studentName;

    // AvailableSession info (clone)
    public Long availableSessionId;
    public String sessionName;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public String description;
    public String type;
    public int minStudents;
    public int maxStudents;
    public int duration;

    // Tutor info
    public Long tutorId;
    public String tutorName;
}
