package com.app.Payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveResponse {
    private String message = "Removed Successfully";
    private String removeType;

    public RemoveResponse(String removeType) {
        this.removeType = removeType;
    }
}
