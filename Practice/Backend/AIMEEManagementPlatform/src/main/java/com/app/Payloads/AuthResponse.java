package com.app.Payloads;

import com.app.Models.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;

    private String tokenType = "Bearer";

    private Roles role;

    private String branchCode;

    public AuthResponse(String accessToken, Roles role,String branchCode) {
        this.accessToken = accessToken;
        this.role = role;
        this.branchCode = branchCode;
    }

    public AuthResponse(String accessToken, Roles role) {
        this.accessToken = accessToken;
        this.role = role;
    }
}
