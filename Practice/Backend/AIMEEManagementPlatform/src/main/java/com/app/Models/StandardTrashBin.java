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
public class StandardTrashBin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String branchCode;

    @NotNull
    private Year academicYearFrom;

    @NotNull
    private Boards board;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Standard standard;

}
