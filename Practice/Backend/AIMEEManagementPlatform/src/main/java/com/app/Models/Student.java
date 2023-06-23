package com.app.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.time.Year;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "students")
public class Student {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studentId;

    @NotNull
    private String studentName;

    @NotNull
    private String studentCode;

    @NotNull
    private String branchCode;

    @NotNull
    private Year yearFrom;

    @NotNull
    private Boards board;

    @NotNull
    private String standard;

    @OneToOne(cascade = CascadeType.ALL)
    private StudentGeneralProfile studentGeneralProfile;

    @OneToOne(cascade = CascadeType.ALL)
    private StudentSportsProfile studentSportsProfile;

    @OneToOne(cascade = CascadeType.ALL)
    private StudentBehaviouralProfile studentBehaviouralProfile;

    @OneToOne(cascade = CascadeType.ALL)
    private StudentCoCurriculumActivityProfile studentCoCurriculumActivityProfile;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(studentCode, student.studentCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentCode);
    }
}
