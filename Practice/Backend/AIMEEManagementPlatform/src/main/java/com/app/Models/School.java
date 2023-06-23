package com.app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer schoolId;

    @NotNull
    private String branchCode;

    @NotNull
    private String branchAddress;

    @OneToMany(cascade = CascadeType.ALL)
    private List<AcademicYear> academicYears;

    @OneToOne(cascade = CascadeType.ALL)
    private User directorOfSchool;

    @OneToOne(cascade = CascadeType.ALL)
    private User principleOfSchool;

    @OneToMany(cascade = CascadeType.ALL)
    private List<User> academicAuditors;

    @OneToMany(cascade = CascadeType.ALL)
    private List<User> adminStaff;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Teacher> teachers;


}
