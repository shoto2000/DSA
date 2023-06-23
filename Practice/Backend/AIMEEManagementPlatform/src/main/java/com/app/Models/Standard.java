package com.app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Standard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer standardId;

    @NotNull
    private String standardLevel;

    @NotNull
    private int noOfStudents = 0;

    @NotNull
    private int noOfSubjects = 0;

    @OneToOne(cascade = CascadeType.ALL)
    private Teacher classTeacher;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Student> students;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Subject> subjects;


}
