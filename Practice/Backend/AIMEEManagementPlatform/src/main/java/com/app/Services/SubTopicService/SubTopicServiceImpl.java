package com.app.Services.SubTopicService;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.*;
import com.app.Payloads.MoveToTrashResponse;
import com.app.Repositories.*;
import com.app.Helpers.FindSubTopicHelper;
import com.app.Helpers.FindTopicHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class SubTopicServiceImpl implements SubTopicService{

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private FindTopicHelper findTopicOOPs;

    @Autowired
    private SubTopicRepository subTopicRepository;

    @Autowired
    private FindSubTopicHelper findSubTopicOOPs;

    @Autowired
    private InquiryQuestionRepository inquiryQuestionRepository;

    @Autowired
    private ReferenceVideoRepository referenceVideoRepository;

    @Autowired
    private ClassRoomActivityRepository classRoomActivityRepository;

    private static final Logger logger = LoggerFactory.getLogger(SubTopicServiceImpl.class);

    @Override
    public ResponseEntity<?> addSubTopic(SubTopic subTopic, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("addSubTopic method invoked");
        logger.info("Fetching the Topic");
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Searching for the Subtopic");
        Optional<SubTopic> existsSubTopic = foundTopic.getSubTopics().stream().filter(s -> s.getSubTopicName().equals(subTopic.getSubTopicName())).findAny();
        logger.info("Checking if the Subtopic is present or not");
        if(!existsSubTopic.isPresent()){
            logger.info("Adding the Subtopic in the Topic");
            foundTopic.getSubTopics().add(subTopic);
            logger.info("Increasing the number of Subtopic inside the Topic");
            foundTopic.setNoOfSubTopics(foundTopic.getNoOfSubTopics()+1);
            logger.info("Saving the Subtopic");
            topicRepository.save(foundTopic);
            logger.info("Getting Subtopic with Id");
            SubTopic getSubTopic = foundTopic.getSubTopics().stream().filter(s -> s.getSubTopicName().equals(subTopic.getSubTopicName())).findAny().get();
            logger.info("Return SubTopic");
            return new ResponseEntity<>(getSubTopic, HttpStatus.CREATED);
        }
        logger.info("Warning Subtopic Already Exist");
        throw new AlreadyExistsException("Subtopic already present");
    }

    @Override
    public ResponseEntity<?> deleteSubTopic(String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        Optional<SubTopic> existsSubTopic = foundTopic.getSubTopics().stream().filter(s -> s.getSubTopicName().equals(subTopicName)).findAny();
        if(existsSubTopic.isPresent()){
            foundTopic.getSubTopics().remove(existsSubTopic.get());
            foundTopic.setNoOfSubTopics(foundTopic.getNoOfSubTopics()-1);
            topicRepository.save(foundTopic);
            return new ResponseEntity<>(new MoveToTrashResponse("Subtopic"), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Subtopic not found");
    }

    @Override
    public ResponseEntity<?> getAllSubtopics(String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("getAllSubtopics method invoked");
        logger.info("Fetching the Topic");
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Returning the Response of all the Subtopics");
        return new ResponseEntity<>(foundTopic.getSubTopics(), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> searchSubtopic(String subTopicName) {
        logger.info("searchSubtopic method invoked");
        logger.info("Searching the Subtopic");
        List<SubTopic> subTopics = subTopicRepository.findBySubTopicNameKey(subTopicName);
        logger.info("checking if the Subtopic is present or not");
        if(!subTopics.isEmpty())
        {
            logger.info("Returning the Response of all the subtopics found");
            return new ResponseEntity<>(subTopics,HttpStatus.FOUND);
        }
        logger.info("Warning Subtopic not Found");
        throw new ResourceNotFoundException("Subtopic not found");
    }

    @Override
    public ResponseEntity<?> getSubtopicById(Integer id) {
        Optional<SubTopic> existSubTopic = subTopicRepository.findById(id);
        if(existSubTopic.isPresent()){
            return new ResponseEntity<>(existSubTopic.get(),HttpStatus.FOUND);
        }
        throw new ResourceNotFoundException("Subtopic not Found");
    }

    @Override
    public ResponseEntity<?> addInquiryQuestion(InquiryQuestions inquiryQuestion, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode){
        logger.info("addInquiryQuestion method invoked");
        logger.info("Fetching the Subtopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Searching the Inquiry Question");
        Optional<InquiryQuestions> existsInquiryQuestions = foundSubTopic.getQuestionsSet().stream().filter(i -> i.getAssignedQuestion().equals(inquiryQuestion.getAssignedQuestion())).findAny();
        logger.info("Checking if the inquiry Question is present or not");
        if(!existsInquiryQuestions.isPresent()){
            logger.info("Fetching all the question present and adding a new Question");
            foundSubTopic.getQuestionsSet().add(inquiryQuestion);
            logger.info("Increase the quantity of number of Questions inside Subtopic by 1");
            foundSubTopic.setNoOfQuestions(foundSubTopic.getNoOfQuestions()+1);
            logger.info("Saving the Question and Return the Subtopic in Response");
            return new ResponseEntity<>(subTopicRepository.save(foundSubTopic),HttpStatus.CREATED);
        }
        logger.info("Warning Question is Already Exist");
        throw new AlreadyExistsException("Question already present");

    }

    @Override
    public ResponseEntity<?> editInquiryQuestion(InquiryQuestions inquiryQuestion, String question, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode){
        logger.info("editInquiryQuestion method invoked");
        logger.info("Fetching the Subopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Searching the Inquiry Question");
        Optional<InquiryQuestions> existsInquiryQuestions = foundSubTopic.getQuestionsSet().stream().filter(i -> i.getAssignedQuestion().equals(question)).findAny();
        logger.info("Checking if the inquiry Question is present or not");
        if(existsInquiryQuestions.isPresent()){
            logger.info("Fetching all the Inquiry Question inside the Subtopic");
            InquiryQuestions getInquiryQuestions = existsInquiryQuestions.get();
            logger.info("Editing the Inquiry Question");
            getInquiryQuestions.setAssignedQuestion(inquiryQuestion.getAssignedQuestion());
            logger.info("Editing the Expected Answer");
            getInquiryQuestions.setExpectedAnswer(inquiryQuestion.getExpectedAnswer());
            logger.info("Saving the Question and Return the Subtopic in Response");
            return new ResponseEntity<>(inquiryQuestionRepository.save(getInquiryQuestions),HttpStatus.CREATED);
        }
        logger.info("Warning Inquiry Question not Found");
        throw new ResourceNotFoundException("Question not found");
    }

    @Override
    public ResponseEntity<?> deleteInquiryQuestion(String question, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode){
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        Optional<InquiryQuestions> existsInquiryQuestions = foundSubTopic.getQuestionsSet().stream().filter(i -> i.getAssignedQuestion().equals(question)).findAny();
        if(existsInquiryQuestions.isPresent()){
            foundSubTopic.getQuestionsSet().remove(existsInquiryQuestions.get());
            foundSubTopic.setNoOfQuestions(foundSubTopic.getNoOfQuestions()-1);
            subTopicRepository.save(foundSubTopic);
            return new ResponseEntity<>(new MoveToTrashResponse("InquiryQuestion"),HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Question not found");
    }

    @Override
    public ResponseEntity<?> getInquiryQuestion(String question, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("getInquiryQuestion method invoked");
        logger.info("Fetching the Subopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Searching the Inquiry Question");
        Optional<InquiryQuestions> existsInquiryQuestions = foundSubTopic.getQuestionsSet().stream().filter(i -> i.getAssignedQuestion().equals(question)).findAny();
        logger.info("Checking if the inquiry Question is present or not");
        if(existsInquiryQuestions.isPresent()){
            logger.info("Returning the Response of the Fetched Inquiry Question");
            return new ResponseEntity<>(existsInquiryQuestions.get(),HttpStatus.FOUND);
        }
        logger.info("Warning Inquiry Question not Found");
        throw new ResourceNotFoundException("Question not found");
    }

    @Override
    public ResponseEntity<?> getAllInquiryQuestion(String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("getAllInquiryQuestion method invoked");
        logger.info("Fetching the Subopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Returning the Response of all the Inquiry Questions");
        return new ResponseEntity<>(foundSubTopic.getQuestionsSet(),HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> addReferenceVideo(ReferenceVideo referenceVideo, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("addReferenceVideo method invoked");
        logger.info("Fetching the Subopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Searching the Reference Video");
        Optional<ReferenceVideo> existsReferenceVideo = foundSubTopic.getReferenceVideos().stream().filter(r -> r.getVideoTopic().equals(referenceVideo.getVideoTopic())).findAny();
        logger.info("Checking if the Reference Video is present or not");
        if(!existsReferenceVideo.isPresent()){
            logger.info("Fetching all the Reference Videos present and adding a new Reference Video");
            foundSubTopic.getReferenceVideos().add(referenceVideo);
            logger.info("Increase the quantity of number of Reference Videos inside Subtopic by 1");
            foundSubTopic.setNoOfReferenceVideo(foundSubTopic.getNoOfReferenceVideo()+1);
            logger.info("Saving the Reference Video and Return the Subtopic in Response");
            return new ResponseEntity<>(subTopicRepository.save(foundSubTopic),HttpStatus.CREATED);
        }
        logger.info("Warning Reference Video Already Exist");
        throw new AlreadyExistsException("Reference video already present");
    }

    @Override
    public ResponseEntity<?> editReferenceVideo(ReferenceVideo referenceVideo, String videoTopicName, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("editReferenceVideo method invoked");
        logger.info("Fetching the Subopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Searching the Reference Video");
        Optional<ReferenceVideo> existsReferenceVideo = foundSubTopic.getReferenceVideos().stream().filter(r -> r.getVideoTopic().equals(videoTopicName)).findAny();
        logger.info("Checking if the Reference Video is present or not");
        if(existsReferenceVideo.isPresent()){
            logger.info("Fetching the Reference Videos present in the Subtopic");
            ReferenceVideo getReferenceVideo = existsReferenceVideo.get();
            logger.info("Editing the Reference Video Link");
            getReferenceVideo.setReferenceVideoLink(referenceVideo.getReferenceVideoLink());
            logger.info("Editing the Reference Video Topic name");
            getReferenceVideo.setVideoTopic(referenceVideo.getVideoTopic());
            logger.info("Editing the Reference Video Platform");
            getReferenceVideo.setReferencePlatform(referenceVideo.getReferencePlatform());
            logger.info("Saving the Reference Video and Return the Subtopic in Response");
            return new ResponseEntity<>(referenceVideoRepository.save(getReferenceVideo),HttpStatus.CREATED);
        }
        logger.info("Warning Reference Video not found");
        throw new ResourceNotFoundException("Reference Video not found");
    }

    @Override
    public ResponseEntity<?> getReferenceVideo(String videoTopicName, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("getReferenceVideo method invoked");
        logger.info("Fetching the Subopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Searching the Reference Video");
        Optional<ReferenceVideo> existsReferenceVideo = foundSubTopic.getReferenceVideos().stream().filter(r -> r.getVideoTopic().equals(videoTopicName)).findAny();
        logger.info("Checking if the Reference Video is present or not");
        if(existsReferenceVideo.isPresent()){
            logger.info("Returning the Response of the Fetched Reference Video");
            return new ResponseEntity<>(existsReferenceVideo.get(),HttpStatus.FOUND);
        }
        logger.info("Warning Reference Video not found");
        throw new ResourceNotFoundException("Reference Video not found");
    }

    @Override
    public ResponseEntity<?> getAllReferenceVideo(String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("getAllReferenceVideo method invoked");
        logger.info("Fetching the Subopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Returning the Response of all the Reference Videos");
        return new ResponseEntity<>(foundSubTopic.getReferenceVideos(),HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> deleteReferenceVideo(String videoTopicName, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        Optional<ReferenceVideo> existsReferenceVideo = foundSubTopic.getReferenceVideos().stream().filter(r -> r.getVideoTopic().equals(videoTopicName)).findAny();
        if(existsReferenceVideo.isPresent()){
            foundSubTopic.getReferenceVideos().remove(existsReferenceVideo.get());
            foundSubTopic.setNoOfReferenceVideo(foundSubTopic.getNoOfReferenceVideo()-1);
            subTopicRepository.save(foundSubTopic);
            return new ResponseEntity<>(new MoveToTrashResponse("Reference Video"),HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Reference Video not found");
    }

    @Override
    public ResponseEntity<?> addClassRoomActivity(ClassRoomActivity classRoomActivity, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("addClassRoomActivity method invoked");
        logger.info("Fetching the Subopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Searching the Classroom Activity");
        Optional<ClassRoomActivity> existsClassRoomActivity = foundSubTopic.getClassRoomActivities().stream().filter(c -> c.getActivityDescription().equals(classRoomActivity.getActivityDescription())).findAny();
        logger.info("Checking if the Classroom Activity is present or not");
        if(!existsClassRoomActivity.isPresent()){
            logger.info("Fetching all the Classroom Activity present and adding a new Classroom Activity");
            foundSubTopic.getClassRoomActivities().add(classRoomActivity);
            logger.info("Increase the quantity of number of Classroom Activity inside Subtopic by 1");
            foundSubTopic.setNoOfClassRoomActivities(foundSubTopic.getNoOfClassRoomActivities()+1);
            logger.info("Saving the Classroom Activity and Return the Subtopic in Response");
            return new ResponseEntity<>(subTopicRepository.save(foundSubTopic),HttpStatus.CREATED);
        }
        logger.info("Warning Classroom Activity Already Exist");
        throw new AlreadyExistsException("Classroom Activity already present");
    }

    @Override
    public ResponseEntity<?> editClassRoomActivity(ClassRoomActivity classRoomActivity, String activityDescription, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("editClassRoomActivity method invoked");
        logger.info("Fetching the Subopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Searching the Classroom Activity");
        Optional<ClassRoomActivity> existsClassRoomActivity = foundSubTopic.getClassRoomActivities().stream().filter(c -> c.getActivityDescription().equals(activityDescription)).findAny();
        logger.info("Checking if the Classroom Activity is present or not");
        if(existsClassRoomActivity.isPresent()){
            logger.info("Fetching the Classroom Activity present in the Subtopic");
            ClassRoomActivity getClassRoomActivity = existsClassRoomActivity.get();
            logger.info("Editing the Activity Description");
            getClassRoomActivity.setActivityDescription(classRoomActivity.getActivityDescription());
            logger.info("Editing the Activity Material Required");
            getClassRoomActivity.setMaterialRequired(classRoomActivity.getMaterialRequired());
            logger.info("Saving the Classroom Activity and Return the Subtopic in Response");
            return new ResponseEntity<>(classRoomActivityRepository.save(getClassRoomActivity),HttpStatus.CREATED);
        }
        logger.info("Warning Classroom Activity not Found");
        throw new ResourceNotFoundException("Classroom Activity not found");
    }

    @Override
    public ResponseEntity<?> getClassRoomActivity(String activityDescription, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("getClassRoomActivity method invoked");
        logger.info("Fetching the Subopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Searching the Classroom Activity");
        Optional<ClassRoomActivity> existsClassRoomActivity = foundSubTopic.getClassRoomActivities().stream().filter(c -> c.getActivityDescription().equals(activityDescription)).findAny();
        logger.info("Checking if the Classroom Activity is present or not");
        if(existsClassRoomActivity.isPresent()){
            logger.info("Returning the Response of the Fetched Classroom Activity");
            return new ResponseEntity<>(existsClassRoomActivity.get(),HttpStatus.FOUND);
        }
        logger.info("Warning Classroom Activity not Found");
        throw new ResourceNotFoundException("Classroom Activity not found");
    }

    @Override
    public ResponseEntity<?> getAllClassRoomActivity(String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("getAllClassRoomActivity method invoked");
        logger.info("Fetching the Subopic");
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        logger.info("Returning the Response of all the Classroom Activity");
        return new ResponseEntity<>(foundSubTopic.getClassRoomActivities(),HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> deleteClassRoomActivity(String activityDescription, String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        SubTopic foundSubTopic = findSubTopicOOPs.findSubTopic(subTopicName,topicName,unitTitle, subjectName, standardLevel, board, yearFrom, branchCode);
        Optional<ClassRoomActivity> existsClassRoomActivity = foundSubTopic.getClassRoomActivities().stream().filter(c -> c.getActivityDescription().equals(activityDescription)).findAny();
        if(existsClassRoomActivity.isPresent()){
            foundSubTopic.getClassRoomActivities().remove(existsClassRoomActivity.get());
            foundSubTopic.setNoOfClassRoomActivities(foundSubTopic.getNoOfClassRoomActivities()-1);
            subTopicRepository.save(foundSubTopic);
            return new ResponseEntity<>(new MoveToTrashResponse("Classroom Activity"),HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Classroom Activity not found");
    }


}
