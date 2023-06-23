package com.app.Services.SubTopicService;

import com.app.Models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;
import java.util.List;

public interface SubTopicService {
    public ResponseEntity<?> addSubTopic(SubTopic subTopic, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> deleteSubTopic(String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> getAllSubtopics(String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> searchSubtopic(String subTopicName);

    public ResponseEntity<?> getSubtopicById(Integer id);

    public ResponseEntity<?> addInquiryQuestion(InquiryQuestions inquiryQuestion,String subTopicName,String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> editInquiryQuestion(InquiryQuestions inquiryQuestion,String question, String subTopicName,String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> deleteInquiryQuestion(String question,String subTopicName,String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> getInquiryQuestion(String question,String subTopicName,String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> getAllInquiryQuestion(String subTopicName,String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);


    public ResponseEntity<?> addReferenceVideo(ReferenceVideo referenceVideo, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> editReferenceVideo(ReferenceVideo referenceVideo, String videoTopicName, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> getReferenceVideo(String videoTopic, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> getAllReferenceVideo(String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> deleteReferenceVideo(String videoTopic, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);



    public ResponseEntity<?> addClassRoomActivity(ClassRoomActivity classRoomActivity, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> editClassRoomActivity(ClassRoomActivity classRoomActivity, String activityDescription, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> getClassRoomActivity(String activityDescription , String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> getAllClassRoomActivity(String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> deleteClassRoomActivity(String activityDescription , String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board,Year yearFrom, String branchCode);

}
