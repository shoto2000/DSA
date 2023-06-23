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
public class BoardTrashBin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer boardTrashBinId;

    @NotNull
    private String branchCode;

    @NotNull
    private Year academicYearFrom;

    @OneToOne(cascade = CascadeType.ALL)
    private SchoolBoard schoolBoard;

}
