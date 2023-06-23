package com.app.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SubjectStandardToTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer subjectStandardId;

    @NotNull
    private String subjectCode;

    @NotNull
    private String standardLevel;

    @NotNull
    private String board;

    @NotNull
    private String yearFrom;

}
