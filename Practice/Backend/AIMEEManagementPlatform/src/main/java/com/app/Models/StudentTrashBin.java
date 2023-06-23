package com.app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Year;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class StudentTrashBin {
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
}
