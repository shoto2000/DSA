package com.app.Controllers;

import com.app.Models.Boards;
import com.app.Models.Topic;
import com.app.Services.TopicService.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Year;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @PostMapping("/add")
    public ResponseEntity<?> addTopic(@Valid @RequestBody Topic topic,
                                      @RequestParam("unit-title") String unitTitle,
                                      @RequestParam("subject-code") String subjectCode,
                                      @RequestParam("standard") String standardLevel,
                                      @RequestParam("board") Boards board,
                                      @RequestParam("year-from") Year yearFrom,
                                      @RequestParam("branch-code") String branchCode)
    {
        return topicService.addTopic(topic, unitTitle, subjectCode, standardLevel, board, yearFrom,branchCode);
    }


    @GetMapping("/get")
    public ResponseEntity<?> getTopic(@RequestParam("topic-name") String topicName,
                                      @RequestParam("unit-title") String unitTitle,
                                      @RequestParam("subject-code") String subjectCode,
                                      @RequestParam("standard") String standardLevel,
                                      @RequestParam("board") Boards board,
                                      @RequestParam("year-from") Year yearFrom,
                                      @RequestParam("branch-code") String branchCode)
    {
        return topicService.getTopic(topicName, unitTitle, subjectCode, standardLevel, board, yearFrom,branchCode);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllTopicInUnit(@RequestParam("unit-title") String unitTitle,
                                               @RequestParam("subject-code") String subjectCode,
                                               @RequestParam("standard") String standardLevel,
                                               @RequestParam("board") Boards board,
                                               @RequestParam("year-from") Year yearFrom,
                                               @RequestParam("branch-code") String branchCode)
    {
        return topicService.getAllTopicInUnit(unitTitle, subjectCode, standardLevel, board, yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTopic(@RequestParam("topic-name") String topicName,
                                         @RequestParam("unit-title") String unitTitle,
                                         @RequestParam("subject-code") String subjectCode,
                                         @RequestParam("standard") String standardLevel,
                                         @RequestParam("board") Boards board,
                                         @RequestParam("year-from") Year yearFrom,
                                         @RequestParam("branch-code") String branchCode)
    {
        return topicService.deleteTopic(topicName, unitTitle, subjectCode, standardLevel, board, yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @PostMapping("/worksheet/add")
    public ResponseEntity<?> addWorksheet(@RequestParam("file") MultipartFile file,
                                          @RequestParam("worksheet-title") String worksheetTitle,
                                          @RequestParam("topic-name") String topicName,
                                          @RequestParam("unit-title") String unitTitle,
                                          @RequestParam("subject-code") String subjectCode,
                                          @RequestParam("standard") String standardLevel,
                                          @RequestParam("board") Boards board,
                                          @RequestParam("year-from") Year yearFrom,
                                          @RequestParam("branch-code") String branchCode) throws IOException {
        return topicService.addWorksheet(file,worksheetTitle,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @GetMapping("/worksheet/download")
    public ResponseEntity<?> downloadWorksheet(@RequestParam("file-name") String fileName,
                                               @RequestParam("topic-name") String topicName,
                                               @RequestParam("unit-title") String unitTitle,
                                               @RequestParam("subject-code") String subjectCode,
                                               @RequestParam("standard") String standardLevel,
                                               @RequestParam("board") Boards board,
                                               @RequestParam("year-from") Year yearFrom,
                                               @RequestParam("branch-code") String branchCode)
    {
        return topicService.downloadWorksheet(fileName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @GetMapping("/worksheet/get/all")
    public ResponseEntity<?> getAllWorksheets(@RequestParam("topic-name") String topicName,
                                              @RequestParam("unit-title") String unitTitle,
                                              @RequestParam("subject-code") String subjectCode,
                                              @RequestParam("standard") String standardLevel,
                                              @RequestParam("board") Boards board,
                                              @RequestParam("year-from") Year yearFrom,
                                              @RequestParam("branch-code") String branchCode){
        return topicService.getAllWorkSheet(topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @DeleteMapping("/worksheet/delete")
    public ResponseEntity<?> deleteWorksheet(@RequestParam("file-name") String fileName,
                                              @RequestParam("topic-name") String topicName,
                                              @RequestParam("unit-title") String unitTitle,
                                              @RequestParam("subject-code") String subjectCode,
                                              @RequestParam("standard") String standardLevel,
                                              @RequestParam("board") Boards board,
                                              @RequestParam("year-from") Year yearFrom,
                                              @RequestParam("branch-code") String branchCode){
        return topicService.deleteWorkSheet(fileName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @PostMapping("/assignment/add")
    public ResponseEntity<?> addAssignmentSheet(@RequestParam("file") MultipartFile file,
                                                @RequestParam("assignmentsheet-title") String assignmentSheetTitle,
                                                @RequestParam("topic-name") String topicName,
                                                @RequestParam("unit-title") String unitTitle,
                                                @RequestParam("subject-code") String subjectCode,
                                                @RequestParam("standard") String standardLevel,
                                                @RequestParam("board") Boards board,
                                                @RequestParam("year-from") Year yearFrom,
                                                @RequestParam("branch-code") String branchCode) throws IOException {
        return topicService.addAssignmentSheet(file,assignmentSheetTitle,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @GetMapping("/assignment/download")
    public ResponseEntity<?> downloadAssignmentSheet(@RequestParam("file-name") String fileName,
                                                     @RequestParam("topic-name") String topicName,
                                                     @RequestParam("unit-title") String unitTitle,
                                                     @RequestParam("subject-code") String subjectCode,
                                                     @RequestParam("standard") String standardLevel,
                                                     @RequestParam("board") Boards board,
                                                     @RequestParam("year-from") Year yearFrom,
                                                     @RequestParam("branch-code") String branchCode)
    {
        return topicService.downloadAssignmentSheet(fileName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @GetMapping("/assignment/get/all")
    public ResponseEntity<?> getAllAssignment(@RequestParam("topic-name") String topicName,
                                              @RequestParam("unit-title") String unitTitle,
                                              @RequestParam("subject-code") String subjectCode,
                                              @RequestParam("standard") String standardLevel,
                                              @RequestParam("board") Boards board,
                                              @RequestParam("year-from") Year yearFrom,
                                              @RequestParam("branch-code") String branchCode){
        return topicService.getAllAssignmentSheet(topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @DeleteMapping("/assignment/delete")
    public ResponseEntity<?> deleteAssignment(@RequestParam("file-name") String fileName,
                                             @RequestParam("topic-name") String topicName,
                                             @RequestParam("unit-title") String unitTitle,
                                             @RequestParam("subject-code") String subjectCode,
                                             @RequestParam("standard") String standardLevel,
                                             @RequestParam("board") Boards board,
                                             @RequestParam("year-from") Year yearFrom,
                                             @RequestParam("branch-code") String branchCode){
        return topicService.deleteAssignmentSheet(fileName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }
}
