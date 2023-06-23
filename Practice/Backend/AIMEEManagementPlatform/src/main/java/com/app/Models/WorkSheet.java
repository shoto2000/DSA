package com.app.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer workSheetId;


    @NotNull
    private String workSheetTopic;

    @JsonIgnore
    @NotNull
    @Lob
    private byte[] workSheet;
}
