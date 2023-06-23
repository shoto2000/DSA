package com.app.Repositories;

import com.app.Models.Boards;
import com.app.Models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    public Optional<Student> findByStudentCode(String studentCode);

    public List<Student> findByStudentCodeOrStudentNameContainingIgnoreCaseOrderByStudentName(String studentCode, String studentName);

    public Page<Student> findAllByBranchCode(String branchCode, Pageable pageable);

    public Page<Student> findAllByBranchCodeAndYearFrom(String branchCode,Year yearFrom,Pageable pageable);

    public Page<Student> findAllByBranchCodeAndYearFromAndBoard(String branchCode,Year yearFrom,Boards board,Pageable pageable);

    public Page<Student> findAllByBranchCodeAndYearFromAndBoardAndStandard(String branchCode,Year yearFrom,Boards board,String standard,Pageable pageable);

    public List<Student> findAllByStandard(String standard,Pageable pageable);

}
