package com.app.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TeacherTrashBin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer teacherId;

    @NotNull
    private String teacherName;

    @NotNull
    @Email
    private String teacherEmail;

    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genders gender;

    @NotNull
    private String branchCode;

    private String classTeacherOfStandard;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<SubjectStandardToTeacher> assignSubjectInAStandard;

}
