package com.app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReferenceVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer videoId;

    @NotNull
    private String videoTopic;

    @Enumerated(value = EnumType.STRING)
    private ReferencePlatforms referencePlatform;

    @NotNull
    private String referenceVideoLink;
}
