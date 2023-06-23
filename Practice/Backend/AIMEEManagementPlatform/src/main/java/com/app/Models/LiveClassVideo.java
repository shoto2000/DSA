package com.app.Models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LiveClassVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer liveClassId;

    @NotNull
    private String liveClassTopic;

    @NotNull
    private String liveClassVideoLink;
}
