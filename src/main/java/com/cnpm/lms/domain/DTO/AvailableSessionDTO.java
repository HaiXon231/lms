package com.cnpm.lms.domain.DTO;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AvailableSessionDTO {
    public Long id;
    
    @NotBlank(message = "Name is required")
    public String name;
    
    public String description;
    
    @Pattern(regexp = "(?i)online|offline", message = "Type must be either online or offline")
    public String type;
    
    @Min(value = 1, message = "minStudents must be at least 1")
    public int minStudents;
    
    @Min(value = 1, message = "maxStudents must be at least 1")
    public int maxStudents;
    
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    public int duration;
    
    public boolean open;

    public Long tutorId;
    public String tutorName;

    @NotNull(message = "Slots cannot be null")
    public List<TimeSlotDTO> slots;

    public static class TimeSlotDTO {
        @NotBlank(message = "Date is required")
        public String date;
        
        @NotBlank(message = "Start time is required")
        public String startTime;
        
        @NotBlank(message = "End time is required")
        public String endTime;
    }

}
