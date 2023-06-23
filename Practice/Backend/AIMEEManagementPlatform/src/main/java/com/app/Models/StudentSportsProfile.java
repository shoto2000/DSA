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
@Table(name = "student_sports_profile")
public class StudentSportsProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer studentSportsProfileId;

    @NotNull
    private String studentCode;

    public StudentSportsProfile(String studentCode) {
        this.studentCode = studentCode;
    }
}
