package com.app.Payloads;

import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.app.Models.Genders;
import com.app.Models.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddUserRequest {

    private String name;
    private String email;
    private String password;
    private Roles role;
    private Genders gender;

   
}
