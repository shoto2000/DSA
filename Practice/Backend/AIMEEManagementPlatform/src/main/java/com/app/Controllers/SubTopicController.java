package com.app.Controllers;

import com.app.Models.*;
import com.app.Services.SubTopicService.SubTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Year;

@RestController
@RequestMapping("/subtopic")
public class SubTopicController {

    @Autowired
    private SubTopicService subTopicService;

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @PostMapping("/add")
    public ResponseEntity<?> addSubTopic(@Valid @RequestBody SubTopic subTopic,
                                         @RequestParam("topic-name") String topicName,
                                         @RequestParam("unit-title") String unitTitle,
                                         @RequestParam("subject-code") String subjectCode,
                                         @RequestParam("standard") String standardLevel,
                                         @RequestParam("board") Boards board,
                                         @RequestParam("year-from") Year yearFrom,
                                         @RequestParam("branch-code") String branchCode)
    {
        return subTopicService.addSubTopic(subTopic,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSubTopic(@RequestParam("subtopic-name") String subTopicName,
                                            @RequestParam("topic-name") String topicName,
                                            @RequestParam("unit-title") String unitTitle,
                                            @RequestParam("subject-code") String subjectCode,
                                            @RequestParam("standard") String standardLevel,
                                            @RequestParam("board") Boards board,
                                            @RequestParam("year-from") Year yearFrom,
                                            @RequestParam("branch-code") String branchCode)
    {
        return subTopicService.deleteSubTopic(subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllSubtopics(@RequestParam("topic-name") String topicName,
                                             @RequestParam("unit-title") String unitTitle,
                                             @RequestParam("subject-code") String subjectCode,
                                             @RequestParam("standard") String standardLevel,
                                             @RequestParam("board") Boards board,
                                             @RequestParam("year-from") Year yearFrom,
                                             @RequestParam("branch-code") String branchCode)
    {
        return subTopicService.getAllSubtopics(topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getSubtopicById(@RequestParam("id") Integer id){
        return subTopicService.getSubtopicById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSubtopic(@RequestParam("subtopic-name") String subTopicName)
    {
        return subTopicService.searchSubtopic(subTopicName);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @PostMapping("/inquiry-question/add")
    public ResponseEntity<?> addInquiryQuestion(@Valid @RequestBody InquiryQuestions inquiryQuestions,
                                                @RequestParam("subtopic-name") String subTopicName,
                                                @RequestParam("topic-name") String topicName,
                                                @RequestParam("unit-title") String unitTitle,
                                                @RequestParam("subject-code") String subjectCode,
                                                @RequestParam("standard") String standardLevel,
                                                @RequestParam("board") Boards board,
                                                @RequestParam("year-from") Year yearFrom,
                                                @RequestParam("branch-code") String branchCode){
        return subTopicService.addInquiryQuestion(inquiryQuestions,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @PutMapping("/inquiry-question/edit")
    public ResponseEntity<?> editInquiryQuestion(@Valid @RequestBody InquiryQuestions inquiryQuestions,
                                                 @RequestParam("question") String question,
                                                 @RequestParam("subtopic-name") String subTopicName,
                                                 @RequestParam("topic-name") String topicName,
                                                 @RequestParam("unit-title") String unitTitle,
                                                 @RequestParam("subject-code") String subjectCode,
                                                 @RequestParam("standard") String standardLevel,
                                                 @RequestParam("board") Boards board,
                                                 @RequestParam("year-from") Year yearFrom,
                                                 @RequestParam("branch-code") String branchCode){
        return subTopicService.editInquiryQuestion(inquiryQuestions,question,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")

    @DeleteMapping("/inquiry-question/delete")
    public ResponseEntity<?> deleteInquiryQuestion(@RequestParam("question") String question,
                                                   @RequestParam("subtopic-name") String subTopicName,
                                                   @RequestParam("topic-name") String topicName,
                                                   @RequestParam("unit-title") String unitTitle,
                                                   @RequestParam("subject-code") String subjectCode,
                                                   @RequestParam("standard") String standardLevel,
                                                   @RequestParam("board") Boards board,
                                                   @RequestParam("year-from") Year yearFrom,
                                                   @RequestParam("branch-code") String branchCode){
        return subTopicService.deleteInquiryQuestion(question,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @GetMapping("/inquiry-question/get")
    public ResponseEntity<?> getInquiryQuestion(@RequestParam("question") String question,
                                                @RequestParam("subtopic-name") String subTopicName,
                                                @RequestParam("topic-name") String topicName,
                                                @RequestParam("unit-title") String unitTitle,
                                                @RequestParam("subject-code") String subjectCode,
                                                @RequestParam("standard") String standardLevel,
                                                @RequestParam("board") Boards board,
                                                @RequestParam("year-from") Year yearFrom,
                                                @RequestParam("branch-code") String branchCode){
        return subTopicService.getInquiryQuestion(question,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @GetMapping("/inquiry-question/get/all")
    public ResponseEntity<?> getAllInquiryQuestion(@RequestParam("subtopic-name") String subTopicName,
                                                   @RequestParam("topic-name") String topicName,
                                                   @RequestParam("unit-title") String unitTitle,
                                                   @RequestParam("subject-code") String subjectCode,
                                                   @RequestParam("standard") String standardLevel,
                                                   @RequestParam("board") Boards board,
                                                   @RequestParam("year-from") Year yearFrom,
                                                   @RequestParam("branch-code") String branchCode){
        return subTopicService.getAllInquiryQuestion(subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")

    @PostMapping("/reference-video/add")
    public ResponseEntity<?> addReferenceVideo(@Valid @RequestBody ReferenceVideo referenceVideo,
                                               @RequestParam("subtopic-name") String subTopicName,
                                               @RequestParam("topic-name") String topicName,
                                               @RequestParam("unit-title") String unitTitle,
                                               @RequestParam("subject-code") String subjectCode,
                                               @RequestParam("standard") String standardLevel,
                                               @RequestParam("board") Boards board,
                                               @RequestParam("year-from") Year yearFrom,
                                               @RequestParam("branch-code") String branchCode){
        return subTopicService.addReferenceVideo(referenceVideo,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")

    @PutMapping("/reference-video/edit")
    public ResponseEntity<?> editReferenceVideo(@Valid @RequestBody ReferenceVideo referenceVideo,
                                                @RequestParam("video-name")  String videoTopicName,
                                                @RequestParam("subtopic-name") String subTopicName,
                                                @RequestParam("topic-name") String topicName,
                                                @RequestParam("unit-title") String unitTitle,
                                                @RequestParam("subject-code") String subjectCode,
                                                @RequestParam("standard") String standardLevel,
                                                @RequestParam("board") Boards board,
                                                @RequestParam("year-from") Year yearFrom,
                                                @RequestParam("branch-code") String branchCode){
        return subTopicService.editReferenceVideo(referenceVideo,videoTopicName,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }


    @GetMapping("/reference-video/get")
    public ResponseEntity<?> getReferenceVideo(@RequestParam("video-name")  String videoTopicName,
                                               @RequestParam("subtopic-name") String subTopicName,
                                               @RequestParam("topic-name") String topicName,
                                               @RequestParam("unit-title") String unitTitle,
                                               @RequestParam("subject-code") String subjectCode,
                                               @RequestParam("standard") String standardLevel,
                                               @RequestParam("board") Boards board,
                                               @RequestParam("year-from") Year yearFrom,
                                               @RequestParam("branch-code") String branchCode){
        return subTopicService.getReferenceVideo(videoTopicName,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }


    @GetMapping("/reference-video/get/all")
    public ResponseEntity<?> getAllReferenceVideo(@RequestParam("subtopic-name") String subTopicName,
                                                  @RequestParam("topic-name") String topicName,
                                                  @RequestParam("unit-title") String unitTitle,
                                                  @RequestParam("subject-code") String subjectCode,
                                                  @RequestParam("standard") String standardLevel,
                                                  @RequestParam("board") Boards board,
                                                  @RequestParam("year-from") Year yearFrom,
                                                  @RequestParam("branch-code") String branchCode){
        return subTopicService.getAllReferenceVideo(subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @DeleteMapping("/reference-video/delete")
    public ResponseEntity<?> deleteReferenceVideo(@RequestParam("video-name")  String videoTopicName,
                                                  @RequestParam("subtopic-name") String subTopicName,
                                                  @RequestParam("topic-name") String topicName,
                                                  @RequestParam("unit-title") String unitTitle,
                                                  @RequestParam("subject-code") String subjectCode,
                                                  @RequestParam("standard") String standardLevel,
                                                  @RequestParam("board") Boards board,
                                                  @RequestParam("year-from") Year yearFrom,
                                                  @RequestParam("branch-code") String branchCode){
        return subTopicService.deleteReferenceVideo(videoTopicName,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @PostMapping("/classroom-activity/add")
    public ResponseEntity<?> addActivity(@Valid @RequestBody ClassRoomActivity classRoomActivity,
                                         @RequestParam("subtopic-name") String subTopicName,
                                         @RequestParam("topic-name") String topicName,
                                         @RequestParam("unit-title") String unitTitle,
                                         @RequestParam("subject-code") String subjectCode,
                                         @RequestParam("standard") String standardLevel,
                                         @RequestParam("board") Boards board,
                                         @RequestParam("year-from") Year yearFrom,
                                         @RequestParam("branch-code") String branchCode){
        return subTopicService.addClassRoomActivity(classRoomActivity,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @PutMapping("/classroom-activity/edit")
    public ResponseEntity<?> editActivity(@Valid @RequestBody ClassRoomActivity classRoomActivity,
                                          @RequestParam("activity-name") String activityName,
                                          @RequestParam("subtopic-name") String subTopicName,
                                          @RequestParam("topic-name") String topicName,
                                          @RequestParam("unit-title") String unitTitle,
                                          @RequestParam("subject-code") String subjectCode,
                                          @RequestParam("standard") String standardLevel,
                                          @RequestParam("board") Boards board,
                                          @RequestParam("year-from") Year yearFrom,
                                          @RequestParam("branch-code") String branchCode){
        return subTopicService.editClassRoomActivity(classRoomActivity,activityName,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }


    @GetMapping("/classroom-activity/get")
    public ResponseEntity<?> getActivity(@RequestParam("activity-name") String activityName,
                                         @RequestParam("subtopic-name") String subTopicName,
                                         @RequestParam("topic-name") String topicName,
                                         @RequestParam("unit-title") String unitTitle,
                                         @RequestParam("subject-code") String subjectCode,
                                         @RequestParam("standard") String standardLevel,
                                         @RequestParam("board") Boards board,
                                         @RequestParam("year-from") Year yearFrom,
                                         @RequestParam("branch-code") String branchCode){
        return subTopicService.getClassRoomActivity(activityName,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @GetMapping("/classroom-activity/get/all")
    public ResponseEntity<?> getActivities(@RequestParam("subtopic-name") String subTopicName,
                                           @RequestParam("topic-name") String topicName,
                                           @RequestParam("unit-title") String unitTitle,
                                           @RequestParam("subject-code") String subjectCode,
                                           @RequestParam("standard") String standardLevel,
                                           @RequestParam("board") Boards board,
                                           @RequestParam("year-from") Year yearFrom,
                                           @RequestParam("branch-code") String branchCode){
        return subTopicService.getAllClassRoomActivity(subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher')")
    @DeleteMapping("/classroom-activity/delete")
    public ResponseEntity<?> deleteActivity(@RequestParam("activity-name") String activityName,
                                            @RequestParam("subtopic-name") String subTopicName,
                                            @RequestParam("topic-name") String topicName,
                                            @RequestParam("unit-title") String unitTitle,
                                            @RequestParam("subject-code") String subjectCode,
                                            @RequestParam("standard") String standardLevel,
                                            @RequestParam("board") Boards board,
                                            @RequestParam("year-from") Year yearFrom,
                                            @RequestParam("branch-code") String branchCode){
        return subTopicService.deleteClassRoomActivity(activityName,subTopicName,topicName,unitTitle,subjectCode,standardLevel,board,yearFrom,branchCode);
    }
}
