package com.app.Services.QueryService;

import com.app.Models.Query;
import com.app.Payloads.QueryRequest;
import org.springframework.http.ResponseEntity;

public interface QueryService {
    public ResponseEntity<?> addQuery(QueryRequest queryRequest);

    public ResponseEntity<?> getAllQueryOfStudentByStudentCode(String studentCode,String keyword, Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> getAllQueryAssignedToTeacherByEmail(String teacherEmail,String keyword, Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> getAllQuery(String keyword,Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> assignQueryToTeacher(String queryCode, String teacherEmail);

    public ResponseEntity<?> resolveQuery(String queryCode);

    public ResponseEntity<?> reOpenQuery(String queryCode);

    public ResponseEntity<?> getAllUnResolvedQuery(String keyword,Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> getAllResolvedQuery(String keyword,Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> getAllStudentsWhoAskedTheQuery(Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> getAllTeacherWhomQueryHaveBeenAssigned(Integer pageNo,Integer pageSize,String name);
}
