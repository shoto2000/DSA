package com.app.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String queryCode;

    @NotNull
    private String queryTitle;

    @Column(length = 1000)
    @NotNull
    private String queryDetail;

    @NotNull
    private String studentCode;

    @NotNull
    @Email
    private String teacherEmail;

    private boolean resolveStatus;

    private LocalDateTime createdDateTime;

    private LocalDateTime updatedDataTime;

}
