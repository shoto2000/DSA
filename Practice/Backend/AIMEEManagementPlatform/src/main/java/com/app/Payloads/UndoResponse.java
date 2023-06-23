package com.app.Payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UndoResponse {

    private String message = "Undo Succesfully";
    private String undoType;

    public UndoResponse(String undoType)
    {
        this.undoType = undoType;
    }

}
