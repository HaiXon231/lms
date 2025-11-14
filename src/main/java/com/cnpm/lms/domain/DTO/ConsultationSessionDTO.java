package com.cnpm.lms.domain.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultationSessionDTO {
    public Long id;
    public String room;

    // thông tin clone từ AvailableSession
    public Long availableSessionId;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;
    public String name;
    public String description;
    public String type;
    public int minStudents;
    public int maxStudents;
    public int duration;

    public Long tutorId;
    public String tutorName;
}
