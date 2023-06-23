package com.app.Repositories;

import com.app.Models.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;

public interface AcademicYearRepository extends JpaRepository<AcademicYear,Integer> {

    public AcademicYear  findByAcademicYearFromAndAcademicYearTo(Year from, Year to);

    public AcademicYear findByAcademicYearFrom(Year from);

}
