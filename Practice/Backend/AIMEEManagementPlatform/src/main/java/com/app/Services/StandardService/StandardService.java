package com.app.Services.StandardService;

import com.app.Models.Boards;
import com.app.Models.Standard;
import org.springframework.http.ResponseEntity;

import java.time.Year;

public interface StandardService {

    public ResponseEntity<?> addStandard(Standard standard, Boards board, Year from, String branchCode);

    public ResponseEntity<?> getStandard(String standardLevel, Boards board, Year yearFrom, String schoolBranchCode);

    public ResponseEntity<?> getAllStandardsInBoard(Boards board, Year yearFrom,String branchCode, Integer pageNo, Integer pageSize, String sortBy);

    public ResponseEntity<?> moveStandardToTrash(String standardLevel,Boards board, Year yearFrom,String branchCode);

}
