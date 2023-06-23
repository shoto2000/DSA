package com.app.Services.TeacherService;

import com.app.Models.SubjectStandardToTeacher;
import com.app.Models.Teacher;
import com.app.Payloads.TeacherRequest;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface TeacherService {

    public ResponseEntity<?> addTeacher(TeacherRequest teacherRequest);

    public ResponseEntity<?> moveTeacherToTrash(String email);

    public ResponseEntity<?> getTeacher(String email);

    public ResponseEntity<?> assignSubjectAndStandardToTeacher(SubjectStandardToTeacher subjectStandardToTeacher,String email);

    public ResponseEntity<?> assignSubjectsAndStandardsToTeacher(Set<SubjectStandardToTeacher> subjectStandardToTeachers, String email);

    public ResponseEntity<?> assignTeacherAsAClassTeacher(String email, String standard);

    public ResponseEntity<?> updateClassTeacher(String email,String Standard);

    public ResponseEntity<?> getAllTeacher(String branchCode,Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> getAllTeacherAvailable(Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> getAllTheSubjectsAssignedToTeacher(String email);

    public ResponseEntity<?> getAllTheStandardsAssignedToTeacher(String email);

}
