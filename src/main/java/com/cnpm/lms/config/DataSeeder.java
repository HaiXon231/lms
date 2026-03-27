package com.cnpm.lms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cnpm.lms.domain.Student;
import com.cnpm.lms.domain.Tutor;
import com.cnpm.lms.repository.StudentRepository;
import com.cnpm.lms.repository.TutorRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TutorRepository tutorRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (studentRepo.count() == 0) {
            Student s = new Student();
            s.setEmail("student@lms");
            s.setPassword(passwordEncoder.encode("123456"));
            s.setName("Nguyen Van A (Sinh Vien)");
            s.setRole("student");
            s.setDepartment("Hệ thống thông tin");
            s.setStatus("ACTIVE");
            s.setEnrollmentYear(2023);
            s.setGpa(3.8);
            s.setAvatarUrl("https://i.pravatar.cc/150?u=student@lms");
            studentRepo.save(s);
            System.out.println("====== [SEEDER] Tạo Sinh Viên Test: student@lms / 123456 ======");
        }

        if (tutorRepo.count() == 0) {
            Tutor t = new Tutor();
            t.setEmail("tutor@lms");
            t.setPassword(passwordEncoder.encode("123456"));
            t.setName("Tran Thi B (Giang Vien)");
            t.setRole("tutor");
            t.setDepartment("Khoa Công Nghệ Thông Tin");
            t.setStatus("ACTIVE");
            t.setExperienceYears(5L);
            t.setEducationLevel("Thạc sĩ");
            t.setAvatarUrl("https://i.pravatar.cc/150?u=tutor@lms");
            tutorRepo.save(t);
            System.out.println("====== [SEEDER] Tạo Giảng Viên Test: tutor@lms / 123456 ======");
        }

        // Đảm bảo các user cũ cũng có avatar
        studentRepo.findAll().forEach(s -> {
            if (s.getAvatarUrl() == null || s.getAvatarUrl().isEmpty()) {
                s.setAvatarUrl("https://i.pravatar.cc/150?u=" + s.getEmail());
                studentRepo.save(s);
            }
        });
        tutorRepo.findAll().forEach(t -> {
            if (t.getAvatarUrl() == null || t.getAvatarUrl().isEmpty()) {
                t.setAvatarUrl("https://i.pravatar.cc/150?u=" + t.getEmail());
                tutorRepo.save(t);
            }
        });
    }
}
