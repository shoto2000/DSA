package com.app.Helpers;

import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.Boards;
import com.app.Models.Subject;
import com.app.Models.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Optional;

@Component
public class FindUnitHelper {

    private static final Logger logger = LoggerFactory.getLogger(FindUnitHelper.class);

    @Autowired
    private FindSubjectHelper findingSubjectOOPs;

    public Unit findUnit(String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode)
    {
        logger.info("FindUnit method invoked");
        logger.info("Fetching the Subject");
        Subject findedSubject = findingSubjectOOPs.findSubject(subjectName,standardLevel,board,yearFrom,branchCode);
        logger.info("Searching for Unit");
        Optional<Unit> existsUnit = findedSubject.getUnits().stream().filter(u -> u.getUnitTitle().equals(unitTitle)).findAny();
        logger.info("Checking if Unit is present or not");
        if(existsUnit.isPresent())
        {
            logger.info("Returning the Unit");
            return existsUnit.get();
        }
        logger.info("Warning Unit not Found");
        throw new ResourceNotFoundException("Unit not found");
    }

}
