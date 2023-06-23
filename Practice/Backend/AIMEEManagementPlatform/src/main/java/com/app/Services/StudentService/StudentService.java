package com.app.Services.StudentService;

import com.app.Exceptions.ResourceNotFoundException;
import com.app.Helpers.CSVHelper;

import com.app.Models.Boards;
import com.app.Models.Standard;
import com.app.Models.Student;
import com.app.Payloads.FilterRequest;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Year;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

    public ResponseEntity<?> addStudent(Student student, String standardLevel, Boards board, Year yearFrom, String branchCode) ;

    public ResponseEntity<?> getStudentByCode(String searchTerm);

    public ResponseEntity<?> getAllStudentsByStandard(String standardLevel, Boards board,Year yearFrom, String branchCode);

    public ResponseEntity<?> getAllStudents(Integer pageNo,Integer pageSize,String name);
    
    public List<?> uploadCsvFile(MultipartFile file,String standardLevel, Boards board, Year yearFrom, String branchCode);

	ResponseEntity<?> downloadCsvFile(String standardLevel, Boards board, Year yearFrom, String branchCode);
	
    public ResponseEntity<?> moveStudentToTrash(String studentCode);

    public ResponseEntity<?> addSubjectToStudent(String subjectCode, String studentCode);

    public ResponseEntity<?> removeSubjectFromStudent(String subjectCode, String studentCode);

    public ResponseEntity<?> getSubjectsByStudentCode(String studentCode);

    public ResponseEntity<?> getStudentsBySubjectCode(String SubjectCode);

    public ResponseEntity<?> filterStudent(FilterRequest filterRequest, Integer pageNo, Integer pageSize, String name);

}
