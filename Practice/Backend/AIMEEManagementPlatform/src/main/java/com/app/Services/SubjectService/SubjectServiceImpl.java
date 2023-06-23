package com.app.Services.SubjectService;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.*;
import com.app.Payloads.MoveToTrashResponse;
import com.app.Repositories.SchoolRepository;
import com.app.Repositories.StandardRepository;
import com.app.Helpers.FindStandardHelper;
import com.app.Repositories.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private FindStandardHelper subjectOops;

    private static final Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

    @Override
    public ResponseEntity<?> addSubject(Subject subject, String standard, Boards board, Year yearFrom, String branchCode) {
        logger.info("addSubject invoked for adding subject in a standard");
        logger.info("Finding Standard using findStandardHelper");
        Standard findingStandard = subjectOops.findStandard(standard,board,yearFrom, branchCode);
        logger.info("Standard found and now finding subject in standard");
        Optional<Subject> existsSubject = findingStandard.getSubjects().stream().filter(s->s.getSubjectCode().equals(subject.getSubjectCode())).findAny();
        if(!existsSubject.isPresent())
        {
            logger.info("Subject Not Found so adding new subject and increasing no of subjects ");
            findingStandard.setNoOfSubjects(findingStandard.getNoOfSubjects()+1);
            findingStandard.getSubjects().add(subject);
            logger.info("Saving subject in standard ");
            standardRepository.save(findingStandard);
            logger.info("Returning subject");
            return new ResponseEntity<>(subject, HttpStatus.CREATED);
        }
        logger.warn("Subject Already Exist So Not Adding Again");
        throw new AlreadyExistsException("Subject Already exists");


    }

    @Override
    public ResponseEntity<?> getSubjectByCode(String subject, String standard, Boards board, Year yearFrom, String branchCode) {
        logger.info("getSubjectByCode invoked for getting subject by subject code");
        logger.info("Finding standard using findStandardHelper");
        Standard findingStandard = subjectOops.findStandard(standard,board,yearFrom, branchCode);
        logger.info("Standard found so now finding subject in that standard");
        Optional<Subject> existsSubject = findingStandard.getSubjects().stream().filter(s->s.getSubjectCode().equals(subject)).findAny();
        if(existsSubject.isPresent())
        {
            logger.info("Subject Found So Returning the subject");
            return new ResponseEntity<>(existsSubject.get(), HttpStatus.FOUND);
        }
        logger.warn("Subject Not Found");
        throw new ResourceNotFoundException("Subject Not Found");
    }

    @Override
    public ResponseEntity<?> getSubjectBySubjectCode(String subjectCode) {
        Subject getSubject = subjectRepository.findBySubjectCode(subjectCode);
        if(getSubject!=null){
            return new ResponseEntity<>(getSubject,HttpStatus.FOUND);
        }
        throw new ResourceNotFoundException("Subject not Found");
    }

    @Override
    public ResponseEntity<?> getAllSubjectsInStandard(String standard, Boards board, Year yearFrom, String branchCode) {
        logger.info("getAllSubjectsInStandard invoked for getting subjects in standard");
        logger.info("Finding standard using findStandardHelper");
        Standard findingStandard = subjectOops.findStandard(standard,board, yearFrom, branchCode);
        return new ResponseEntity<>(findingStandard.getSubjects(),HttpStatus.FOUND);
    }

}
