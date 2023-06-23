package com.app.Controllers;

import com.app.Models.Boards;
import com.app.Models.Unit;
import com.app.Services.UnitService.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Year;

@RestController
@RequestMapping("/unit")
public class UnitController {

    @Autowired
    private UnitService unitService;

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @PostMapping("/add")
    public ResponseEntity<?> addUnit(@Valid @RequestBody Unit unit,
                                     @RequestParam("subject-code") String subjectCode,
                                     @RequestParam("standard") String standard,
                                     @RequestParam("board") Boards board,
                                     @RequestParam("year-from") Year yearFrom,
                                     @RequestParam("branch-code")String branchCode)
    {
        return unitService.addUnit(unit,subjectCode,standard,board,yearFrom,branchCode);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getUnit(@RequestParam("unit-title") String unitTitle,
                                     @RequestParam("subject-code") String subjectCode,
                                     @RequestParam("standard") String standard,
                                     @RequestParam("board") Boards board,
                                     @RequestParam("year-from") Year yearFrom,
                                     @RequestParam("branch-code")String branchCode)
    {
        return unitService.getUnit(unitTitle,subjectCode,standard,board,yearFrom,branchCode);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllUnitsInSubject(@RequestParam("subject-code") String subjectCode,
                                                  @RequestParam("standard") String standard,
                                                  @RequestParam("board") Boards board,
                                                  @RequestParam("year-from") Year yearFrom,
                                                  @RequestParam("branch-code")String branchCode)
    {
        return unitService.getAllUnitsInSubject(subjectCode,standard,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUnit(@RequestParam("unit-title") String unitTitle,
                                        @RequestParam("subject-code") String subjectCode,
                                        @RequestParam("standard") String standard,
                                        @RequestParam("board") Boards board,
                                        @RequestParam("year-from") Year yearFrom,
                                        @RequestParam("branch-code")String branchCode)
    {
        return unitService.deleteUnit(unitTitle,subjectCode,standard,board,yearFrom,branchCode);
    }
}
