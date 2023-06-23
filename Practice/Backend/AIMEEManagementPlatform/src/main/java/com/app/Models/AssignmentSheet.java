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
public class AssignmentSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer assignmentId;

    @NotNull
    private String assignmentTopic;

    @JsonIgnore
    @NotNull
    @Lob
    private byte[] assignment;

}
