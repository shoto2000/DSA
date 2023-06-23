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
public class AcademicYearTrashBin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer binId;

    @NotNull
    private String branchCode;

    @OneToOne
    private AcademicYear academicYears;


}
