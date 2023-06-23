package com.app.Repositories;



import com.app.Models.AcademicYear;
import com.app.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Models.School;

import java.time.Year;


@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {

	public School findByBranchCode(String BranchCode);

	public School findByDirectorOfSchool(User user);

	public School findByPrincipleOfSchool(User user);

	public School findByAcademicAuditors(User user);

	public School findByAdminStaff(User user);

	public School findByTeachers(User user);

}
