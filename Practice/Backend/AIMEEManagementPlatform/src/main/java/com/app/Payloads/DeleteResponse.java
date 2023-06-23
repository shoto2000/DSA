package com.app.Payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeleteResponse {

    private String message = "Delete Successfully";
    private String deleteType;

    public DeleteResponse(String deleteType)
    {
        this.deleteType = deleteType;
    }

}
