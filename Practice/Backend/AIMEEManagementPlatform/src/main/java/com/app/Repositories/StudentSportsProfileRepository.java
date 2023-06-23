package com.app.Repositories;

import com.app.Models.StudentGeneralProfile;
import com.app.Models.StudentSportsProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentSportsProfileRepository extends JpaRepository<StudentSportsProfile,Integer> {

    @Transactional
    @Query(value = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'student_sports_profile' AND COLUMN_NAME = :columnName ", nativeQuery = true)
    public Integer columnExistsOrNoT(String columnName);

    @Transactional
    @Query(value = "SELECT * FROM student_sports_profile WHERE student_code = :studentCode",nativeQuery = true)
    public String findProfile(String studentCode);

    @Transactional
    @Query(value = "SELECT column_name FROM information_schema.columns WHERE table_name = 'student_sports_profile' AND column_key != 'MUL' ORDER BY ORDINAL_POSITION",nativeQuery = true)
    public List<String> findColumns();

    @Transactional
    @Query(value = "SELECT column_name,data_type FROM information_schema.columns WHERE table_name = 'student_sports_profile'", nativeQuery = true)
    List<String[]> getTableMetadata();

    public StudentSportsProfile findByStudentCode(String studentCode);

}
