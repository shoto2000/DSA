package com.app.Services.UnitService;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.*;
import com.app.Payloads.ApiResponse;
import com.app.Payloads.MoveToTrashResponse;
import com.app.Repositories.SubjectRepository;
import com.app.Helpers.FindSubjectHelper;
import com.app.Repositories.UnitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.Optional;

@Service
public class UnitServiceImpl implements UnitService {

    @Autowired
    private FindSubjectHelper findingSubjectOOPs;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UnitRepository unitRepository;

    private static final Logger logger = LoggerFactory.getLogger(UnitServiceImpl.class);

    @Override
    public ResponseEntity<?> addUnit(Unit unit, String subjectCode, String standard, Boards board, Year yearFrom, String branchCode) {
        logger.info("addUnit method invoked");
        logger.info("Fetching the Subject");
        Subject findingSubject = findingSubjectOOPs.findSubject(subjectCode,standard,board,yearFrom,branchCode);
        logger.info("Searching for Unit");
        Optional<Unit> existsUnit = findingSubject.getUnits().stream().filter(u -> u.getUnitTitle().equals(unit.getUnitTitle())).findAny();
        logger.info("Checking if Unit is present or not");
        if(!existsUnit.isPresent()){
            logger.info("Adding the unit inside the Subject");
            findingSubject.getUnits().add(unit);
            logger.info("Increasing the number of Unit inside the Subject by 1");
            findingSubject.setNoOfUnits(findingSubject.getNoOfUnits()+1);
            logger.info("Saving the Unit and Return Subject as the Response");
            return new ResponseEntity<>(subjectRepository.save(findingSubject), HttpStatus.CREATED);
        }
        logger.info("Warning Unit Already Exists");
        throw new AlreadyExistsException("Unit already present");

    }

    @Override
    public ResponseEntity<?> getUnit(String unitTitle, String subjectCode, String standard, Boards board, Year yearFrom, String branchCode) {
        logger.info("getUnit method invoked");
        logger.info("Fetching the Subject");
        Subject findingSubject = findingSubjectOOPs.findSubject(subjectCode,standard,board,yearFrom,branchCode);
        logger.info("Searching for Unit");
        Optional<Unit> existsUnit = findingSubject.getUnits().stream().filter(u -> u.getUnitTitle().equals(unitTitle)).findAny();
        logger.info("Checking if Unit is present or not");
        if(existsUnit.isPresent()){
            logger.info("Return the Unit in the Response");
            return new ResponseEntity<>(existsUnit.get(), HttpStatus.FOUND);
        }
        logger.info("Warning Unit not Found");
        throw new ResourceNotFoundException("Unit not found");
    }

    @Override
    public ResponseEntity<?> getAllUnitsInSubject(String subjectCode, String standard, Boards board, Year yearFrom, String branchCode) {
        logger.info("getAllUnitsInSubject method invoked");
        logger.info("Fetching the Subject");
        Subject findingSubject = findingSubjectOOPs.findSubject(subjectCode,standard,board,yearFrom,branchCode);
        logger.info("Return the List of Units in the Response");
        return new ResponseEntity<>(findingSubject.getUnits(),HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> deleteUnit(String unitTitle, String subjectCode, String standard, Boards board, Year yearFrom, String branchCode) {
        Subject findingSubject = findingSubjectOOPs.findSubject(subjectCode,standard,board,yearFrom,branchCode);
        Optional<Unit> existsUnit = findingSubject.getUnits().stream().filter(u -> u.getUnitTitle().equals(unitTitle)).findAny();
        if(existsUnit.isPresent()){
            findingSubject.setNoOfUnits(findingSubject.getNoOfUnits()-1);
            findingSubject.getUnits().remove(existsUnit.get());
            subjectRepository.save(findingSubject);
            return new ResponseEntity<>(new MoveToTrashResponse("Unit"), HttpStatus.OK
            );
        }
        throw new ResourceNotFoundException("Unit Not Found");
    }

}
