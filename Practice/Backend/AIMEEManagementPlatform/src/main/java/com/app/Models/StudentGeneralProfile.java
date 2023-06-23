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
@Table(name = "student_general_profile")
public class StudentGeneralProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer generalProfileId;

    @NotNull
    private String studentCode;

    public StudentGeneralProfile(String studentCode) {
        this.studentCode = studentCode;
    }
}
