package com.cnpm.lms.domain.DTO;

public class SingleAvailableSessionDTO {
    public Long id;
    public String date;
    public String startTime;
    public String endTime;
    public String name;
    public String description;
    public String type;
    public int minStudents;
    public int maxStudents;
    public int duration;
    public boolean open;

    public Long tutorId;
    public String tutorName;
}
