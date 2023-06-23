package com.app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ClassRoomActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer acitivityId;

    @NotNull
    private String activityDescription;

    @NotNull
    private String materialRequired;
}
