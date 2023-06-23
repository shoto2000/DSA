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
public class InquiryQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer questionId;

    @Column(length = 1000)
    @NotNull
    private String assignedQuestion;

    @Column(length = 100000)
    @NotNull
    private String expectedAnswer;


}
