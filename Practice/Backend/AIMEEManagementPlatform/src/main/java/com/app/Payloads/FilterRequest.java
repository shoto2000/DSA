package com.app.Payloads;

import com.app.Models.Boards;
import lombok.Data;

import java.time.Year;

@Data
public class FilterRequest {
    private String branchCode;
    private Year yearFrom;
    private Boards board;
    private String standard;
}
