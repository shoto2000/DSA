package com.app.Helpers;

import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.*;
import com.app.Repositories.AcademicYearRepository;
import com.app.Repositories.SchoolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Optional;

@Component
public class FindStandardHelper {

    private static final Logger logger = LoggerFactory.getLogger(FindStandardHelper.class);

    @Autowired
    private FindBoardHelper findBoardHelper;

    public Standard findStandard(String standardLevel, Boards board, Year yearFrom, String branchCode)
    {
        logger.info("FindStandard method invoked");
        logger.info("Fetching the School Board");
        SchoolBoard foundBoard = findBoardHelper.findBoard(board,yearFrom,branchCode);
        logger.info("Searching for Standard");
        Optional<Standard> standard = foundBoard.getStandards().stream().filter(s -> s.getStandardLevel().equals(standardLevel)).findAny();
        logger.info("Checking if Standard is present or not");
        if(standard.isPresent())
        {
            logger.info("Returning the Standard");
            return standard.get();
        }
        logger.info("Warning Standard not Found");
        throw new ResourceNotFoundException("Standard Not Found");
    }
}
