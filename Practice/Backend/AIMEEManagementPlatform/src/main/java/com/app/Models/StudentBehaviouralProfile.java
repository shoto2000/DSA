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
@Table(name = "student_behavioural_profile")
public class StudentBehaviouralProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer behaviouralProfileId;

    @NotNull
    private String studentCode;

    public StudentBehaviouralProfile(String studentCode) {
        this.studentCode = studentCode;
    }
}
