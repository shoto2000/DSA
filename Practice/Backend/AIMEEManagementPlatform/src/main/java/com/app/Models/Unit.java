package com.app.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer unitId;

    @NotNull
    private String unitTitle;

    @NotNull
    private int noOfTopics = 0;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Topic> topics;

}
