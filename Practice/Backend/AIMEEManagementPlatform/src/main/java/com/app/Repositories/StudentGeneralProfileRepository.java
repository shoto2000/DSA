package com.app.Repositories;

import com.app.Models.StudentGeneralProfile;
import com.app.Payloads.ColumnsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentGeneralProfileRepository extends JpaRepository<StudentGeneralProfile,Integer> {

    @Transactional
    @Query(value = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'student_general_profile' AND COLUMN_NAME = :columnName ", nativeQuery = true)
    public Integer columnExistsOrNoT(String columnName);

    @Transactional
    @Query(value = "SELECT * FROM student_general_profile WHERE student_code = :studentCode",nativeQuery = true)
    public String findProfile(String studentCode);

    @Transactional
    @Query(value = "SELECT column_name FROM information_schema.columns WHERE table_name = 'student_general_profile' AND column_key != 'MUL' ORDER BY ORDINAL_POSITION",nativeQuery = true)
    public List<String> findColumns();

    @Transactional
    @Query(value = "SELECT column_name,data_type FROM information_schema.columns WHERE table_name = 'student_general_profile'", nativeQuery = true)
    List<String[]> getTableMetadata();

    public StudentGeneralProfile findByStudentCode(String studentCode);

}
