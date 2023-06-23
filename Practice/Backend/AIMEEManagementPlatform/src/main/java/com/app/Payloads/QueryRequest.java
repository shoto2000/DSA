package com.app.Payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequest {

    @NotNull
    private String studentCode;

    @NotNull
    private String queryTitle;

    @NotNull
    @Column(length = 1000)
    private String queryDescription;

}
