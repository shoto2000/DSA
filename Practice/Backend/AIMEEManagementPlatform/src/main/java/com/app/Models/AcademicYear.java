package com.app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Year;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer academicYearId;

    @NotNull
    private Year academicYearFrom;

    @NotNull
    private Year academicYearTo;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SchoolBoard> boards;

}
