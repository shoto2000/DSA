package com.app.Services.UnitService;

import com.app.Models.Boards;
import com.app.Models.Unit;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Year;

public interface UnitService {
    public ResponseEntity<?> addUnit(Unit unit, String subjectCode, String standard, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> getUnit(String unitName, String subjectCode, String standard, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> getAllUnitsInSubject(String subjectCode, String standard, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> deleteUnit(String unitName, String subjectCode, String standard, Boards board, Year yearFrom, String branchCode);
}
