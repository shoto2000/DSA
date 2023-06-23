package com.app.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer topicId;

    @NotNull
    private String topicName;

    @NotNull
    private int noOfSubTopics = 0;

    @NotNull
    private int noOfWorkSheets = 0;

    @NotNull
    private int noOfAssignmentSheet = 0;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<WorkSheet> workSheets;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<AssignmentSheet>  assignmentSheets;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<SubTopic> subTopics;

}
