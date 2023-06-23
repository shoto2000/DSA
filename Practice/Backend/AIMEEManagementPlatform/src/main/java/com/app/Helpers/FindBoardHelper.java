package com.app.Helpers;

import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.*;
import com.app.Repositories.AcademicYearRepository;
import com.app.Repositories.SchoolRepository;
import com.app.Services.UserService.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Optional;

@Component
public class FindBoardHelper {

    @Autowired
    private SchoolRepository schoolRepository;

    private static final Logger logger = LoggerFactory.getLogger(FindBoardHelper.class);


    public SchoolBoard findBoard(Boards board, Year academicYearfrom, String branchCode)
    {
        logger.info("FindBoard method invoked");
        logger.info("Finding School Branch By Branch Code");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School Branch is present or not");
        if (school != null) {
            logger.info("Searching the Academic year");
            Optional<AcademicYear> existsAcademicYear = school.getAcademicYears().stream().filter(a -> a.getAcademicYearFrom().equals(academicYearfrom)).findAny();
            logger.info("Checking if Academic year is present or not");
            if(existsAcademicYear.isPresent())
            {
                logger.info("Fetching the Academic year");
                AcademicYear academicYear = existsAcademicYear.get();
                logger.info("Searching for School Board");
                Optional<SchoolBoard> schoolBoard = academicYear.getBoards().stream().filter(b -> b.getBoard().equals(board)).findAny();
                logger.info("Checking if School Board is present or not");
                if(schoolBoard.isPresent())
                {
                    logger.info("Returning the School Board");
                    return schoolBoard.get();
                }
                logger.info("Warning School Board not Found");
                throw new ResourceNotFoundException("School Board Not Found");
            }
            logger.info("Warning Academic year not Found");
            throw new ResourceNotFoundException("Academic Year Not Found");
        }
        logger.info("Warning School not Found");
        throw new ResourceNotFoundException("School Branch not found");
    }

}
