package com.app.Services.TopicService;

import com.app.Models.Boards;
import com.app.Models.Topic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;

public interface TopicService {
    public ResponseEntity<?> addTopic(Topic topic, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> getTopic(String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> getAllTopicInUnit(String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> deleteTopic(String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> addWorksheet(MultipartFile file ,String worksheetTitle,String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) throws IOException;

    public ResponseEntity<?> downloadWorksheet(String fileName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> getAllWorkSheet(String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> deleteWorkSheet(String fileName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> addAssignmentSheet(MultipartFile file,String assignmentSheetTitle, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) throws IOException;

    public ResponseEntity<?> downloadAssignmentSheet(String fileName,String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> getAllAssignmentSheet(String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> deleteAssignmentSheet(String fileName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode);

}
