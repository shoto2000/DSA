package com.app.Repositories;

import com.app.Models.AcademicYearTrashBin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;

public interface AcademicYearTrashBinRepository extends JpaRepository<AcademicYearTrashBin,Integer> {
    public AcademicYearTrashBin findByAcademicYearsAcademicYearFrom(Year yearFrom);
}
