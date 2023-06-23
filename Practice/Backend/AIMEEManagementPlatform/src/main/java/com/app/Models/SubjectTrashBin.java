package com.app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Year;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SubjectTrashBin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer subjectTrashBin;

    @NotNull
    private String branchCode;

    @NotNull
    private Year yearFrom;

    @NotNull
    private Boards board;

    @NotNull
    private String standard;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Subject subjects;

}
