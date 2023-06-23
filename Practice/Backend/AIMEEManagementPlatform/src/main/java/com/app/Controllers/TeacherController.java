package com.app.Controllers;

import com.app.Models.SubjectStandardToTeacher;
import com.app.Payloads.TeacherRequest;
import com.app.Services.TeacherService.TeacherService;
import com.app.Services.TrashBinService.TrashBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@PreAuthorize("hasAnyRole('Administrator','Principal','Director','AdminStaff','AcademicAuditor')")
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TrashBinService trashBinService;

    @PostMapping("/add")
    public ResponseEntity<?> addTeacher(@Valid @RequestBody TeacherRequest teacher) {
        return teacherService.addTeacher(teacher);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','AdminStaff','AcademicAuditor','Teacher')")
    @GetMapping("/get")
    public ResponseEntity<?> getTeacher(@RequestParam("email") String email) {
        return teacherService.getTeacher(email);
    }

    @PutMapping("/add/assign-subject-standard")
    public ResponseEntity<?> assignSubjectAndStandardToTeacher(@RequestBody SubjectStandardToTeacher subjectStandardToTeacher, @RequestParam("email") String email) {
        return teacherService.assignSubjectAndStandardToTeacher(subjectStandardToTeacher, email);
    }


    @PutMapping("/add/assign-list-of-subjects-standards")
    public ResponseEntity<?> assignSubjectsAndStandardsToTeacher(@RequestBody Set<SubjectStandardToTeacher> subjectStandardToTeachers, @RequestParam("email") String email) {
        return teacherService.assignSubjectsAndStandardsToTeacher(subjectStandardToTeachers, email);
    }

    @PutMapping("/add/class-teacher")
    public ResponseEntity<?> assignTeacherAsAClassTeacher(@RequestParam("email") String email, @RequestParam("standard") String standard) {
        return teacherService.assignTeacherAsAClassTeacher(email, standard);
    }

    @PutMapping("/update/class-teacher")
    public ResponseEntity<?> updateClassTeacher(@RequestParam("email") String email, @RequestParam("standard") String standard) {
        return teacherService.updateClassTeacher(email, standard);
    }


    @GetMapping("/branch/get/all")
    public ResponseEntity<?> getAllTeacherInBranch(@RequestParam("branch-code") String branchCode, @RequestParam("page-no") Integer pageNo, @RequestParam("page-size") Integer pageSize, @RequestParam("sort-by") String fieldName) {
        return teacherService.getAllTeacher(branchCode, pageNo, pageSize, fieldName);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllTeachers(@RequestParam("page-no") Integer pageNo, @RequestParam("page-size") Integer pageSize, @RequestParam("sort-by") String fieldName){
        return teacherService.getAllTeacherAvailable(pageNo,pageSize,fieldName);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> moveTeacherToTrash(@RequestParam("email") String email) {
        return teacherService.moveTeacherToTrash(email);
    }

    @DeleteMapping("/trash/undo")
    public ResponseEntity<?> undoTeacherFromTrash(@RequestParam("email") String email) {
        return trashBinService.undoTeacherFromTrashBin(email);
    }

    @DeleteMapping("/trash/delete")
    public ResponseEntity<?> deleteTeacherFromTrashPermanently(@RequestParam("email") String email){
        return trashBinService.deleteTeacherFromTrash(email);
    }

    @GetMapping("/trash/get/all")
    public ResponseEntity<?> getAllTeacherFromTrash(@RequestParam("page-no")Integer pageNo,
                                                    @RequestParam("page-size")Integer pageSize,
                                                    @RequestParam("sort-by")String sortBy) {
        return trashBinService.getAllTeachersFromTrash(pageNo,pageSize,sortBy);
    }

    @DeleteMapping("/trash/delete/all")
    public ResponseEntity<?> emptyTrashBinForTeacher(){
        return trashBinService.deleteAllTeacherFromTrash();
    }

    @GetMapping("/subject/get/all")
    public ResponseEntity<?> getAllSubjectsAssignedToTeacher(@RequestParam("email") String email){
        return teacherService.getAllTheSubjectsAssignedToTeacher(email);
    }

    @GetMapping("/standard/get/all")
    public ResponseEntity<?> getAllStandardsAssignedToTeacher(@RequestParam("email") String email){
        return teacherService.getAllTheStandardsAssignedToTeacher(email);
    }

}
