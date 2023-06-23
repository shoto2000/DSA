package com.app.Payloads;

import com.app.Models.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordResponse {

    private Roles resetUserType;
    private String message = "Password reset successfully";

    public ResetPasswordResponse(Roles resetUserType) {
        this.resetUserType = resetUserType;
    }
}
