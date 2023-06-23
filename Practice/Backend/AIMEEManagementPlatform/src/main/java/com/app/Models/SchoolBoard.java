package com.app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SchoolBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer boardId;

    @NotNull
    private Boards board;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Standard> standards;


}
