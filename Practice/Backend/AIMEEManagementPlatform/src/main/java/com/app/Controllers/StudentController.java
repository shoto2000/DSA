package com.app.Controllers;


import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.Boards;
import com.app.Models.Student;
import com.app.Payloads.FilterRequest;
import com.app.Repositories.StudentGeneralProfileRepository;
import com.app.Services.StudentService.Behaviour.StudentBehaviouralProfileService;
import com.app.Services.StudentService.CoCurriculum.StudentCoCurriculumActivityProfileService;
import com.app.Services.StudentService.General.StudentGeneralProfileService;
import com.app.Services.StudentService.Sport.StudentSportsProfileService;
import com.app.Services.StudentService.StudentService;
import com.app.Services.TrashBinService.TrashBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentGeneralProfileService studentGeneralProfileService;

    @Autowired
    private StudentGeneralProfileRepository studentGeneralProfileRepository;

    @Autowired
    private StudentSportsProfileService studentSportsProfileService;

    @Autowired
    private StudentBehaviouralProfileService studentBehaviouralProfileService;

    @Autowired
    private StudentCoCurriculumActivityProfileService studentCoCurriculumActivityProfileService;

    @Autowired
    private TrashBinService trashBinService;

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@RequestBody Student student, @RequestParam("standard") String standardLevel, @RequestParam("board") Boards board, @RequestParam("year-from") Year yearFrom, @RequestParam("branch-code") String branchCode) {
        return studentService.addStudent(student, standardLevel, board, yearFrom, branchCode);
    }

    
    @GetMapping("/get/by-code-or-name")
    public ResponseEntity<?> getStudentByCode(@RequestParam("search-term") String searchTerm) {
        return studentService.getStudentByCode(searchTerm);
    }

    @GetMapping("/get/all/by-standard")
    public ResponseEntity<?> getAllStudentsByStandard(@RequestParam("standard") String standardLevel,
                                                      @RequestParam("board") Boards board,
                                                      @RequestParam("year-from") Year yearFrom,
                                                      @RequestParam("branch-code") String branchCode) {
        return studentService.getAllStudentsByStandard(standardLevel, board, yearFrom, branchCode);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllStudents(@RequestParam("page-no") Integer pageNo, @RequestParam("page-size") Integer pageSize, @RequestParam("sort-by") String fieldName) {
        return studentService.getAllStudents(pageNo, pageSize, fieldName);
    }

    @PostMapping("get/filter")
    public ResponseEntity<?> filterStudent(@RequestBody FilterRequest filterRequest,
                                           @RequestParam("page-no") Integer pageNo,
                                           @RequestParam("page-size") Integer pageSize,
                                           @RequestParam("sort-by") String fieldName){
        return studentService.filterStudent(filterRequest,pageNo,pageSize,fieldName);
    }

    @PostMapping("/subject/add")
    public ResponseEntity<?> addSubjectToStudent(@RequestParam("student-code") String studentCode, @RequestParam("subject-code") String subjectCode) {
        return studentService.addSubjectToStudent(subjectCode,studentCode);
    }

    @DeleteMapping("/subject/delete")
    public ResponseEntity<?> removeSubjectFromStudent(@RequestParam("student-code") String studentCode, @RequestParam("subject-code") String subjectCode) {
        return studentService.removeSubjectFromStudent(subjectCode,studentCode);
    }

    @GetMapping("/subject/get/subjects")
    public ResponseEntity<?> getSubjectsByStudentCode(@RequestParam("student-code") String studentCode){
        return studentService.getSubjectsByStudentCode(studentCode);
    }

    @GetMapping("/subject/get/students")
    public ResponseEntity<?> getStudentsBySubjectCode(@RequestParam("subject-code") String subjectCode){
        return studentService.getStudentsBySubjectCode(subjectCode);
    }

    @DeleteMapping("trash/move")
    public ResponseEntity<?> moveStudentToTrash(@RequestParam("student-code") String studentCode){
        return studentService.moveStudentToTrash(studentCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/trash/undo")
    public ResponseEntity<?> undoStudentFromTrash(@RequestParam("student-code") String studentCode){
        return trashBinService.undoStudentFromTrashBin(studentCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/trash/delete")
    public ResponseEntity<?> deleteStudentFromTrashPermanent(@RequestParam("student-code") String studentCode){
        return trashBinService.deleteStudentFromTrash(studentCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @GetMapping("/trash/get/all")
    public ResponseEntity<?> getAllStudentFromTrash(@RequestParam("page-no")Integer pageNo,
                                                    @RequestParam("page-size")Integer pageSize,
                                                    @RequestParam("sort-by")String sortBy){
        return trashBinService.getAllStudentFromTrash(pageNo,pageSize,sortBy);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @DeleteMapping("/trash/delete/all")
    public ResponseEntity<?> EmptyTrashBinStudents(){
        return trashBinService.deleteAllStudentFromTrash();
    }

    @PostMapping("/profile/general/add/column")
    public ResponseEntity<?> addNewColumn(@RequestParam("column-name") String key, @RequestParam("column-data-type") String dataType) {
        return studentGeneralProfileService.addColumnToProfile(key, dataType);
    }

    @PostMapping("/profile/general/add/value")
    public ResponseEntity<?> addValueToColumn(@RequestParam("student-code") String studentCode, @RequestParam("column-name") String key, @RequestParam("value") String value) {
        return studentGeneralProfileService.addValueToProfile(studentCode, key, value);
    }

    @GetMapping("/profile/general/get")
    public ResponseEntity<?> getStudentProfile(@RequestParam("student-code") String studentCode) {
        return studentGeneralProfileService.getGeneralProfileOfStudent(studentCode);
    }

    @GetMapping("/profile/general/get/columns")
    public ResponseEntity<?> getStudentProfileColumnsAndDatatype() {
        return studentGeneralProfileService.getColumnsAndDataTypeOfTable();
    }

    @PutMapping("/profile/general/edit/column")
    public ResponseEntity<?> updateColumnName(@RequestParam("column-name")String columnName,@RequestParam("new-column-name") String newColumnName)
    {
        return studentGeneralProfileService.updateColumnName(columnName, newColumnName);
    }

    @PostMapping("/profile/sports/add/column")
    public ResponseEntity<?> addNewColumnInSportsProfile(@RequestParam("column-name") String key, @RequestParam("column-data-type") String dataType) {
        return studentSportsProfileService.addColumnToProfile(key, dataType);
    }

    @PostMapping("/profile/sports/add/value")
    public ResponseEntity<?> addValueToColumnInSportsProfile(@RequestParam("student-code") String studentCode, @RequestParam("column-name") String key, @RequestParam("value") String value) {
        return studentSportsProfileService.addValueToProfile(studentCode, key, value);
    }

    @GetMapping("/profile/sports/get")
    public ResponseEntity<?> getStudentSportsProfile(@RequestParam("student-code") String studentCode) {
        return studentSportsProfileService.getSportsProfileOfStudent(studentCode);
    }

    @GetMapping("/profile/sports/get/columns")
    public ResponseEntity<?> getStudentSportsProfile() {
        return studentSportsProfileService.getAllColumnsAndDatatypeofSportsProfile();
    }

    @PutMapping("/profile/sports/edit/column")
    public ResponseEntity<?> updateColumnNameOfStudentProfile(@RequestParam("column-name") String columnName,@RequestParam("new-column-name") String newColumnName) {
        return studentSportsProfileService.updateColumnNameOfSportsProfile(columnName, newColumnName);
    }

    @PostMapping("/profile/behavioural/add/column")
    public ResponseEntity<?> addNewColumnInBehaviouralProfile(@RequestParam("column-name") String key, @RequestParam("column-data-type") String dataType) {
        return studentBehaviouralProfileService.addColumnToProfile(key, dataType);
    }

    @PostMapping("/profile/behavioural/add/value")
    public ResponseEntity<?> addValueToColumnInBehaviouralProfile(@RequestParam("student-code") String studentCode, @RequestParam("column-name") String key, @RequestParam("value") String value) {
        return studentBehaviouralProfileService.addValueToProfile(studentCode, key, value);
    }

    @GetMapping("/profile/behavioural/get")
    public ResponseEntity<?> getStudentBehaviouralProfile(@RequestParam("student-code") String studentCode) {
        return studentBehaviouralProfileService.getBehaviouralProfileOfStudent(studentCode);
    }

    @GetMapping("/profile/behavioural/get/columns")
    public ResponseEntity<?> getAllColumnsOfBehaviouralProfile() {
        return studentBehaviouralProfileService.getAllColumnsAndDatatype();
    }

    @PutMapping("/profile/behavioural/edit/column")
    public ResponseEntity<?> updateColumnOfBehaviouralProfile(@RequestParam("column-name") String columnName,@RequestParam("new-column-name") String newColumnName)
    {
        return studentBehaviouralProfileService.updateBehaviourColumnProfile(columnName, newColumnName);
    }

    @PostMapping("/profile/co-curriculum-activity/add/column")
    public ResponseEntity<?> addNewColumnInCoCurriculumActivityProfile(@RequestParam("column-name") String key, @RequestParam("column-data-type") String dataType) {
        return studentCoCurriculumActivityProfileService.addColumnToProfile(key, dataType);
    }

    @PostMapping("/profile/co-curriculum-activity/add/value")
    public ResponseEntity<?> addValueToColumnInCoCurriculumActivityProfile(@RequestParam("student-code") String studentCode, @RequestParam("column-name") String key, @RequestParam("value") String value) {
        return studentCoCurriculumActivityProfileService.addValueToProfile(studentCode, key, value);
    }

    @GetMapping("/profile/co-curriculum-activity/get")
    public ResponseEntity<?> getStudentCoCurriculumActivityProfile(@RequestParam("student-code") String studentCode) {
        return studentCoCurriculumActivityProfileService.getProfileOfStudent(studentCode);
    }

    @GetMapping("/profile/co-curriculum-activity/get/columns")
    public ResponseEntity<?> getAllColumnsOfCocurriculamProfile() {
        return studentCoCurriculumActivityProfileService.getAllColumns();
    }

    @PutMapping("/profile/co-curriculum-activity/edit/column")
    public ResponseEntity<?> updateColumnNameOfCoCurriculumProfile(@RequestParam("column-name") String columnName,@RequestParam("new-column-name") String newColumnName)
    {
        return studentCoCurriculumActivityProfileService.updateCoCurriculumProfileOfStudent(columnName, newColumnName);
    }

    
    @PostMapping("/upload-csv-file")
    public List<?> uploadCsvFile(@RequestParam("file") MultipartFile file ,  @RequestParam("standard") String standardLevel, @RequestParam("board") Boards board, @RequestParam("year-from") Year yearFrom, @RequestParam("branch-code") String branchCode) {
        return studentService.uploadCsvFile(file,standardLevel,board,yearFrom,branchCode);
    }

    @GetMapping("/download-csv/file")
    public ResponseEntity<?> downloadCsvFile(@RequestParam("standard") String standardLevel, @RequestParam("board") Boards board, @RequestParam("year-from") Year yearFrom, @RequestParam("branch-code") String branchCode) {
        return studentService.downloadCsvFile(standardLevel,board,yearFrom,branchCode);
    }

    
    }
    

