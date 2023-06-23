package com.app.Services.TrashBinService;

import com.app.Models.*;
import org.springframework.http.ResponseEntity;

import java.time.Year;
import java.util.List;

public interface TrashBinService {

    public void moveAcademicYearToTrashBin(String branchCode, AcademicYear academicYear);

    public ResponseEntity<?> undoAcademicYearFromTrashBin(Year yearFrom);

    public ResponseEntity<List<AcademicYear>> getAllAcademicYearsInTrash();

    public ResponseEntity<?> deleteAcademicYearPermanently(Year yearFrom);

    public ResponseEntity<?> deleteAllAcademicYearsPermanently();

    public ResponseEntity<?> moveBoardToTrashBin(String branchCode, Year academicYear,Boards board);

    public ResponseEntity<?> undoBoardFromTrashBin(Boards board);

    public ResponseEntity<List<SchoolBoard>> getAllBoardInTrash();

    public ResponseEntity<?> deleteBoardPermanently(Boards board);

    public ResponseEntity<?> deleteAllBoardsPermanently();

    public void moveStandardToTrashBin(String branchCode, Year yearFrom, Boards board, Standard standard);

    public ResponseEntity<?> undoStandardFromTrashBin(String standard);

    public ResponseEntity<List<Standard>> getAllStandardInTrashBin();

    public ResponseEntity<?> deleteStandardPermanently(String standard);

    public ResponseEntity<?> deleteAllStandardsPermanently();

    public void moveUserToTrashBin(User user);

    public void moveTeacherToTrashBin(Teacher teacher);

    public void moveStudentToTrashBin(Student student);

    public ResponseEntity<?> undoUserFromTrash(String email);

    public ResponseEntity<?> undoTeacherFromTrashBin(String email);

    public ResponseEntity<?> undoStudentFromTrashBin(String studentCode);

    public ResponseEntity<?> deleteUserFromTrash(String email);

    public ResponseEntity<?> deleteTeacherFromTrash(String email);

    public ResponseEntity<?> deleteStudentFromTrash(String studentCode);

    public ResponseEntity<?> deleteAllUserFromTrash();

    public ResponseEntity<?> deleteAllTeacherFromTrash();

    public ResponseEntity<?> deleteAllStudentFromTrash();

    public ResponseEntity<?> getAllUsersInTrash(Integer pageNo,Integer pageSize,String sortBy);

    public ResponseEntity<?> getAllTeachersFromTrash(Integer pageNo,Integer pageSize,String sortBy);

    public ResponseEntity<?> getAllStudentFromTrash(Integer pageNo,Integer pageSize,String sortBy);

    public ResponseEntity<?> moveSubjectToTrash(String branchCode,Year from,Boards board,String standard,String subjectCode);

    public ResponseEntity<?> undoSubjectFromTrash(String subjectCode);

    public ResponseEntity<?> getAllSubjectsInTrash(Integer pageNo,Integer pageSize,String sortBy);

    public ResponseEntity<?> deleteSubjectPermanentlyFromTrash(String subjecCode);

    public ResponseEntity<?> deleteAllSubjectsPermanentlyFromTrash();


}
