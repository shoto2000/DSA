package com.app.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer subjectId;

    @NotNull
    private String subjectName;

    @NotNull
    private String subjectCode;

    @OneToOne(cascade = CascadeType.ALL)
    private User teacherAssigned;

    @NotNull
    private int noOfUnits = 0;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Unit> units;

    @ManyToOne(cascade = CascadeType.ALL)
    private Standard standard;

    @JsonIgnore
    @ManyToMany(mappedBy = "subjects")
    private Set<Student> students;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(subjectCode, subject.subjectCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectCode);
    }
}
