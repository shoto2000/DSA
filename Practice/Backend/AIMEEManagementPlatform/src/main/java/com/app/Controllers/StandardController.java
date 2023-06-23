package com.app.Controllers;

import com.app.Models.Boards;
import com.app.Models.Standard;
import com.app.Services.StandardService.StandardService;
import com.app.Services.TrashBinService.TrashBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.time.Year;

@RestController
@RequestMapping("/standard")
public class StandardController {

    @Autowired
    private StandardService standardService;

    @Autowired
    private TrashBinService trashBinService;

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @PutMapping("/add")
    public ResponseEntity<?> addStandard(@RequestBody Standard standard,
                                         @RequestParam("board") Boards board,
                                         @RequestParam("year-from") Year yearFrom,
                                         @RequestParam("branch-code") String branchCode)
    {
        return standardService.addStandard(standard,board,yearFrom,branchCode);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getStandard(@RequestParam("standard") String standardLevel,
                                         @RequestParam("board") Boards board,
                                         @RequestParam("year-from") Year yearFrom,
                                         @RequestParam("branch-code") String branchCode)
    {
        return standardService.getStandard(standardLevel,board,yearFrom,branchCode);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllStandardsInBoard(@RequestParam("board") Boards board,
                                                    @RequestParam("year-from") Year yearFrom,
                                                    @RequestParam("branch-code") String branchCode,
                                                    @RequestParam("page-no") Integer pageNo,
                                                    @RequestParam("page-size") Integer pageSize,
                                                    @RequestParam("sort-by") String sortBy)
    {
        return  standardService.getAllStandardsInBoard(board,yearFrom,branchCode,pageNo,pageSize,sortBy);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/trash/move")
    public ResponseEntity<?> moveStandardToTrash(@RequestParam("standard") String standardLevel,
                                            @RequestParam("board")Boards board,
                                            @RequestParam("year-from") Year yearFrom,
                                            @RequestParam("branch-code") String branchCode)
    {
        return standardService.moveStandardToTrash(standardLevel,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/trash/undo")
    public ResponseEntity<?> undoStandardFromTrash(@RequestParam("standard") String standard)
    {
        return trashBinService.undoStandardFromTrashBin(standard);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','AdminStaff','Teacher')")
    @GetMapping ("/trash/get/all")
    public ResponseEntity<?> getAllStandardsInTrash()
    {
        return trashBinService.getAllStandardInTrashBin();
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/trash/delete")
    public ResponseEntity<?> deleteStandardFromTrash(@RequestParam("standard") String standard)
    {
        return trashBinService.deleteStandardPermanently(standard);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/trash/delete/all")
    public ResponseEntity<?> deleteAllStandardFromTrash()
    {
        return trashBinService.deleteAllStandardsPermanently();
    }

}
