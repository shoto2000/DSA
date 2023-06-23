package com.app.Helpers;

import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.AcademicYear;
import com.app.Models.School;
import com.app.Repositories.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Optional;

@Component
public class FindAcademicYearHelper {

    @Autowired
    private SchoolRepository schoolRepository;

    public AcademicYear findAcademicYear(Year academicYearFrom, String branchCode)
    {
        School school = schoolRepository.findByBranchCode(branchCode);
        if(school!=null)
        {
            Optional<AcademicYear> academicYear = school.getAcademicYears().stream().filter(s -> s.getAcademicYearFrom().equals(academicYearFrom)).findAny();
            return academicYear.orElse(null);
        }
        throw new ResourceNotFoundException("School not found");
    }

}
