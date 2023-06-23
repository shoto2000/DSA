package com.app.Services.SchoolService;

import com.app.Models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;
import java.util.List;


@Service
public interface SchoolService {
    public ResponseEntity<?> addSchool(School addSchool);

    public ResponseEntity<?> addAcademicYearToSchool(AcademicYear academicYear, String branchCode);

    public ResponseEntity<?> getAcademicYear(Year yearFrom , String branchCode);

    public ResponseEntity<?> getAllAcademicYears(String branchCode);

    public ResponseEntity<?> uploadDataToAcademicYear(String branchCode, Year yearFrom, MultipartFile file) throws IOException;

    public ResponseEntity<?> downloadAcademicYearFromDatabase(String branchCode,Year yearFrom);

    public ResponseEntity<?> moveYearToTrash(Year yearFrom , String branchCode);

    public ResponseEntity<?> updateBranchAddress(School school);

    public ResponseEntity<?> deleteSchool(String branchCode);

    public ResponseEntity<?> getSchool(String branchCode);

    public ResponseEntity<?> getAllBranches(Integer pageNo,Integer pageSize,String name);

    public void assignDirector(String branchCode, User user);

    public void assignPrincipal(String branchCode, User user);

    public void addAdminStaff(String branchCode, User user);

    public void addAcademicAuditor(String branchCode,User user);

    public ResponseEntity<?> addBoard(Boards board, Year academicYearfrom, String branchCode);

    public ResponseEntity<?> getBoard(Boards board, Year academicYearfrom, String branchCode);

    public ResponseEntity<?> getAllBoards(String branchCode,Year academicYearfrom);
}
