package com.app.Controllers;

import com.app.Models.Boards;
import com.app.Models.Subject;
import com.app.Services.SubjectService.SubjectService;
import com.app.Services.TrashBinService.TrashBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Year;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TrashBinService trashBinService;

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @PostMapping("/add")
    public ResponseEntity<?> addSubject(@Valid @RequestBody Subject subject,
                                        @RequestParam("standard") String standard,
                                        @RequestParam("board")Boards board,
                                        @RequestParam("year-from") Year yearFrom,
                                        @RequestParam("branch-code")String branchCode)
    {
        return subjectService.addSubject(subject, standard, board, yearFrom, branchCode);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getSubjectByCode(@RequestParam String subject,
                                              @RequestParam("standard") String standard,
                                              @RequestParam("board")Boards board,
                                              @RequestParam("year-from") Year yearFrom,
                                              @RequestParam("branch-code")String branchCode)
    {
        return subjectService.getSubjectByCode(subject, standard, board, yearFrom, branchCode);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllSubjectsInStandard(@RequestParam("standard") String standard,
                                                      @RequestParam("board")Boards board,
                                                      @RequestParam("year-from") Year yearFrom,
                                                      @RequestParam("branch-code")String branchCode)
    {
        return subjectService.getAllSubjectsInStandard(standard, board, yearFrom, branchCode);
    }

    @GetMapping("/get/unique")
    public ResponseEntity<?> getSubjectBySubjectCode(@RequestParam("subject-code") String subjectCode){
        return subjectService.getSubjectBySubjectCode(subjectCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/trash/move")
    public ResponseEntity<?> moveToTrashSubject(@RequestParam("subject-code") String subjectCode,
                                           @RequestParam("standard") String standard,
                                           @RequestParam("board")Boards board,
                                           @RequestParam("year-from") Year yearFrom,
                                           @RequestParam("branch-code")String branchCode)
    {
        return trashBinService.moveSubjectToTrash(branchCode, yearFrom, board, standard, subjectCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/trash/undo")
    public ResponseEntity<?> undoSubjectFromTrash(@RequestParam("subject-code") String subjectCode)
    {
        return trashBinService.undoSubjectFromTrash(subjectCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @GetMapping("trash/get/all")
    public ResponseEntity<?> getAllSubjectsInTrash(@RequestParam("page-no")Integer pageNo,
                                                   @RequestParam("page-size")Integer pageSize,
                                                   @RequestParam("sort-by")String sortBy)
    {
        return trashBinService.getAllSubjectsInTrash(pageNo,pageSize,sortBy);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/trash/delete")
    public ResponseEntity<?> deleteSubjectPermanentlyFromTrash(@RequestParam("subject-code") String subjectCode)
    {
        return trashBinService.deleteSubjectPermanentlyFromTrash(subjectCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/trash/delete/all")
    public ResponseEntity<?> deleteAllSubjectPermanentlyFromTrash()
    {
        return trashBinService.deleteAllSubjectsPermanentlyFromTrash();
    }


}
