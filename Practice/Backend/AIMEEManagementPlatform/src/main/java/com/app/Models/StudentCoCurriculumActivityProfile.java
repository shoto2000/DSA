package com.app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "student_co_curriculum_activity_profile")
public class StudentCoCurriculumActivityProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer coCurriculumProfileId;

    @NotNull
    private String studentCode;

    public StudentCoCurriculumActivityProfile(String studentCode) {
        this.studentCode = studentCode;
    }
}
