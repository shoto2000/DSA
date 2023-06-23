package com.app.Payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveToTrashResponse {
    private String message = "Moved to trash Successfully";
    private String deleteType;

    public MoveToTrashResponse(String deleteType) {
        this.deleteType = deleteType;
    }
}
