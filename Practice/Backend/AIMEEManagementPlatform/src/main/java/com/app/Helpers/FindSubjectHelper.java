package com.app.Helpers;

import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.*;
import com.app.Repositories.SchoolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Optional;

@Component
public class FindSubjectHelper {

    private static final Logger logger = LoggerFactory.getLogger(FindSubjectHelper.class);

    @Autowired
    private FindStandardHelper findStandardOOps;

    public Subject findSubject(String subjectCode, String standard, Boards board, Year yearFrom, String branchCode)
    {
        logger.info("FindSubject method invoked");
        logger.info("Fetching the Standard");
        Standard findStandard = findStandardOOps.findStandard(standard,board,yearFrom,branchCode);
        logger.info("Searching for Subject");
        Optional<Subject> existingSubject = findStandard.getSubjects().stream().filter(s->s.getSubjectCode().equals(subjectCode)).findAny();
        logger.info("Checking if Subject is present or not");
        if(existingSubject.isPresent()){
            logger.info("Returning the Subject");
            return existingSubject.get();
        }
        logger.info("Warning Subject not Found");
        throw new ResourceNotFoundException("Subject not found");
    }
}
