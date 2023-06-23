package com.app.Services.TopicService;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.*;
import com.app.Payloads.ApiResponse;
import com.app.Payloads.DeleteResponse;
import com.app.Payloads.MoveToTrashResponse;
import com.app.Repositories.TopicRepository;
import com.app.Repositories.UnitRepository;
import com.app.Helpers.FindTopicHelper;
import com.app.Helpers.FindUnitHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;
import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService{

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private FindUnitHelper findUnitOOps;

    @Autowired
    private FindTopicHelper findTopicOOPs;

    @Autowired
    private TopicRepository topicRepository;

    private static final Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);

    @Override
    public ResponseEntity<?> addTopic(Topic topic, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("addTopic() invoked for adding topic and finding unit using findUntiHelper");
        Unit foundUnit = findUnitOOps.findUnit(unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Unit found so finding topic in unit");
        Optional<Topic> existsTopic = foundUnit.getTopics().stream().filter(t -> t.getTopicName().equals(topic.getTopicName())).findAny();
        if(!existsTopic.isPresent())
        {
            logger.info("Topic already not exists so we are adding new topic");
            foundUnit.getTopics().add(topic);
            foundUnit.setNoOfTopics(foundUnit.getNoOfTopics()+1);
            logger.info("Saving topic");
            unitRepository.save(foundUnit);
            logger.info("Getting topic with Id");
            Topic getTopic = foundUnit.getTopics().stream().filter(t -> t.getTopicName().equals(topic.getTopicName())).findAny().get();
            logger.info("Return Added Topic");
            return new ResponseEntity<>(getTopic, HttpStatus.CREATED);
        }
        logger.warn("Topic Already Exists so try to add some other");
        throw new AlreadyExistsException("Topic Already Exists");
    }

    @Override
    public ResponseEntity<?> getTopic(String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("getTopic() invoked for getting topic and finding unit using findUnitHelper");
        Unit findedUnit = findUnitOOps.findUnit(unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Unit found so finding topic in unit");
        Optional<Topic> existsTopic = findedUnit.getTopics().stream().filter(t -> t.getTopicName().equals(topicName)).findAny();
        if(existsTopic.isPresent())
        {
            logger.info("Topic found so returning the topic");
            return new ResponseEntity<>(existsTopic, HttpStatus.FOUND);
        }
        logger.warn("Topic not found try to add");
        throw new ResourceNotFoundException("Topic Not Found");
    }

    @Override
    public ResponseEntity<?> getAllTopicInUnit(String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("getAllTopicInUnit() invoked for getting all topic in unit and finding unit using findUnitHelper");
        Unit findedUnit = findUnitOOps.findUnit(unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Returning topics in unit");
        return new ResponseEntity<>(findedUnit.getTopics(),HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> deleteTopic(String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        Unit findedUnit = findUnitOOps.findUnit(unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        Optional<Topic> existsTopic = findedUnit.getTopics().stream().filter(t -> t.getTopicName().equals(topicName)).findAny();
        if(existsTopic.isPresent())
        {
            findedUnit.getTopics().remove(existsTopic.get());
            findedUnit.setNoOfTopics(findedUnit.getNoOfTopics()-1);
            unitRepository.save(findedUnit);
            return new ResponseEntity<>(new MoveToTrashResponse("Topic"), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Topic Not Found");
    }

    @Override
    public ResponseEntity<?> addWorksheet(MultipartFile file, String worksheetTitle, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) throws IOException {
        logger.info("addWorksheet invoked for adding worksheet file and finding topic");
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board, yearFrom,branchCode);
        logger.info("finding worksheet already exists or not");
        Optional<WorkSheet> existsWorkSheet = foundTopic.getWorkSheets().stream().filter(i -> i.getWorkSheetTopic().equals(worksheetTitle)).findAny();
        if(!existsWorkSheet.isPresent()){
            try {
                logger.info("Working sheet not added yet so adding now");
                WorkSheet worksheet = new WorkSheet();
                worksheet.setWorkSheetTopic(worksheetTitle);
                worksheet.setWorkSheet(file.getBytes());
                foundTopic.getWorkSheets().add(worksheet);
                foundTopic.setNoOfWorkSheets(foundTopic.getNoOfWorkSheets()+1);
                topicRepository.save(foundTopic);
                return new ResponseEntity<>(new ApiResponse(true,"Assignment Added"),HttpStatus.CREATED);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
            }
        }
        logger.info("Worksheet already present so add someone else");
        throw new AlreadyExistsException("Worksheet already present");
    }

    @Override
    public ResponseEntity<?> downloadWorksheet(String fileName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("downloadWorksheet() invoked for download worksheet and finding topic");
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board, yearFrom,branchCode);
        logger.info("Finding worksheet already exists or not");
        Optional<WorkSheet> existsWorkSheets = foundTopic.getWorkSheets().stream().filter(i -> i.getWorkSheetTopic().equals(fileName)).findAny();
        if (existsWorkSheets.isPresent()) {
            logger.info("Worksheet already exists so return worksheet");
            WorkSheet workSheets = existsWorkSheets.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(workSheets.getWorkSheetTopic()).build());
            return new ResponseEntity<>(workSheets.getWorkSheet(), headers, HttpStatus.OK);
        }
        logger.warn("Worksheet not found try to add");
        throw new ResourceNotFoundException("Worksheet not found");
    }

    @Override
    public ResponseEntity<?> getAllWorkSheet(String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board, yearFrom,branchCode);

        return new ResponseEntity<>(foundTopic.getWorkSheets(),HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> deleteWorkSheet(String fileName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board, yearFrom,branchCode);
        Optional<WorkSheet> existsWorkSheets = foundTopic.getWorkSheets().stream().filter(i -> i.getWorkSheetTopic().equals(fileName)).findAny();
        if(existsWorkSheets.isPresent()){
            foundTopic.getWorkSheets().remove(existsWorkSheets.get());
            foundTopic.setNoOfWorkSheets(foundTopic.getNoOfWorkSheets()-1);
            topicRepository.save(foundTopic);
            return new ResponseEntity<>(new DeleteResponse("Worksheet"),HttpStatus.ACCEPTED);
        }
        throw new ResourceNotFoundException("Worksheet not Found");

    }

    @Override
    public ResponseEntity<?> addAssignmentSheet(MultipartFile file,String assignmentSheetTitle, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) throws IOException {
        logger.info("addAssignmentSheet() invoked and finding topic already exists or not");
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board, yearFrom,branchCode);
        logger.info("Finding assignment sheet in topic");
        Optional<AssignmentSheet> existsAssignmentSheet = foundTopic.getAssignmentSheets().stream().filter(i -> i.getAssignmentTopic().equals(assignmentSheetTitle)).findAny();
        if(!existsAssignmentSheet.isPresent()){
            try {
                logger.info("Assignment sheet not found so adding new one");
                AssignmentSheet assignmentSheet = new AssignmentSheet();
                assignmentSheet.setAssignmentTopic(assignmentSheetTitle);
                assignmentSheet.setAssignment(file.getBytes());
                foundTopic.getAssignmentSheets().add(assignmentSheet);
                foundTopic.setNoOfAssignmentSheet(foundTopic.getNoOfAssignmentSheet()+1);
                topicRepository.save(foundTopic);
                return new ResponseEntity<>(new ApiResponse(true,"Assignment Sheet added"),HttpStatus.CREATED);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
            }
        }
        logger.warn("Assignment sheet already present");
        throw new AlreadyExistsException("Assignment Sheet file is present");
    }

    @Override
    public ResponseEntity<?> downloadAssignmentSheet(String fileName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("downloadAssignmentSheet() invoked for downloading assignment sheet and finding topic");
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board, yearFrom,branchCode);
        logger.info("Finding assignment sheet");
        Optional<AssignmentSheet> existsAssignmentSheet = foundTopic.getAssignmentSheets().stream().filter(i -> i.getAssignmentTopic().equals(fileName)).findAny();
        if (existsAssignmentSheet.isPresent()) {
            logger.info("Assignment sheet found so preparing for download");
            AssignmentSheet assignmentSheet = existsAssignmentSheet.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(assignmentSheet.getAssignmentTopic()).build());
            logger.info("Downloading sheet");
            return new ResponseEntity<>(assignmentSheet.getAssignment(), headers, HttpStatus.OK);
        }
        logger.warn("File not found try to add file");
        throw new ResourceNotFoundException("File not found");
    }

    @Override
    public ResponseEntity<?> getAllAssignmentSheet(String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board, yearFrom,branchCode);

        return new ResponseEntity<>(foundTopic.getAssignmentSheets(),HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> deleteAssignmentSheet(String fileName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board, yearFrom,branchCode);
        Optional<AssignmentSheet> existsAssignmentSheet = foundTopic.getAssignmentSheets().stream().filter(i -> i.getAssignmentTopic().equals(fileName)).findAny();
        if(existsAssignmentSheet.isPresent()){
            foundTopic.getAssignmentSheets().remove(existsAssignmentSheet.get());
            foundTopic.setNoOfAssignmentSheet(foundTopic.getNoOfAssignmentSheet()-1);
            topicRepository.save(foundTopic);
            return new ResponseEntity<>(new DeleteResponse("Assignment"),HttpStatus.ACCEPTED);
        }
        throw new ResourceNotFoundException("Assignment not Found");
    }
}
