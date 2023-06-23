package com.app.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @Lob
    private byte[] image;

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @JsonIgnore
    @Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{8,40}$",message="Password should be at least 8 character and maximum 40 characters.")
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genders gender;

    private String branchCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Roles role;
}
