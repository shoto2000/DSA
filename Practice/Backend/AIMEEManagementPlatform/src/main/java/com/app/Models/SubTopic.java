package com.app.Models;

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
public class SubTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer subTopicId;

    @NotNull
    private String subTopicName;

    @Column(length = 100000)
    @NotNull
    private String description;

    @NotNull
    private int noOfQuestions = 0;

    @NotNull
    private int noOfReferenceVideo = 0;

    @NotNull
    private int noOfClassRoomActivities = 0;

    @OneToMany(cascade = CascadeType.ALL)
    private List<InquiryQuestions> questionsSet;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ReferenceVideo> referenceVideos;

    @OneToMany(cascade = CascadeType.ALL)
    private List<LiveClassVideo> liveClassVideos;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ClassRoomActivity> classRoomActivities;
}
