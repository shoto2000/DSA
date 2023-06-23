package com.app.Services.SubjectService;

import com.app.Models.Boards;
import com.app.Models.Subject;
import org.springframework.http.ResponseEntity;

import java.time.Year;

public interface SubjectService {
    public ResponseEntity<?> addSubject(Subject subject, String standard, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> getSubjectByCode(String subject, String standard, Boards board, Year yearFrom, String branchCode);

    public ResponseEntity<?> getSubjectBySubjectCode(String subjectCode);

    public ResponseEntity<?> getAllSubjectsInStandard(String standard, Boards board, Year yearFrom, String branchCode);

}
