package com.app.Controllers;

import com.app.Models.Query;
import com.app.Payloads.QueryRequest;
import com.app.Services.QueryService.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@PreAuthorize("hasAnyRole('Administrator','AdminStaff')")
@RequestMapping("/query")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @PostMapping("/add")
    public ResponseEntity<?> addQuery(@RequestBody @Valid QueryRequest request){
        return queryService.addQuery(request);
    }

    @GetMapping("/get/student")
    public ResponseEntity<?> getAllQueryStudent(@RequestParam("student-code") String studentCode,
                                                @RequestParam("keyword") String keyword,
                                                @RequestParam("page-no") Integer pageNo,
                                                @RequestParam("page-size") Integer pageSize,
                                                @RequestParam("sort-by") String sortBy){
        return queryService.getAllQueryOfStudentByStudentCode(studentCode,keyword,pageNo,pageSize,sortBy);
    }

    @PreAuthorize("hasAnyRole('Administrator','AdminStaff','Teacher')")
    @GetMapping("/get/teacher")
    public ResponseEntity<?> getAllQueryTeacher(@RequestParam("email") String teacherEmail,
                                                @RequestParam("keyword") String keyword,
                                                @RequestParam("page-no") Integer pageNo,
                                                @RequestParam("page-size") Integer pageSize,
                                                @RequestParam("sort-by") String sortBy){
        return queryService.getAllQueryAssignedToTeacherByEmail(teacherEmail,keyword,pageNo,pageSize,sortBy);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllQuery(@RequestParam("keyword") String keyword,
                                         @RequestParam("page-no") Integer pageNo,
                                         @RequestParam("page-size") Integer pageSize,
                                         @RequestParam("sort-by") String sortBy){
        return queryService.getAllQuery(keyword,pageNo,pageSize,sortBy);
    }

    @PutMapping("/assign/teacher")
    public ResponseEntity<?> assignQueryToTeacher(@RequestParam("query-code") String queryCode,@RequestParam("email") @Email String teacherEmail){
        return queryService.assignQueryToTeacher(queryCode,teacherEmail);
    }

    @PreAuthorize("hasAnyRole('Administrator','AdminStaff','Teacher')")
    @PutMapping("/resolve")
    public ResponseEntity<?> resolveStatus(@RequestParam("query-code") String queryCode){
        return queryService.resolveQuery(queryCode);
    }

    @PutMapping("/re-open")
    public ResponseEntity<?> reOpenQuery(@RequestParam("query-code") String queryCode){
        return queryService.reOpenQuery(queryCode);
    }

    @GetMapping("/get/all/resolved")
    public ResponseEntity<?> getAllResolved(@RequestParam("keyword") String keyword,
                                            @RequestParam("page-no") Integer pageNo,
                                            @RequestParam("page-size") Integer pageSize,
                                            @RequestParam("sort-by") String sortBy){
        return queryService.getAllResolvedQuery(keyword,pageNo,pageSize,sortBy);
    }

    @GetMapping("/get/all/un-resolved")
    public ResponseEntity<?> getAllUnResolved(@RequestParam("keyword") String keyword,
                                              @RequestParam("page-no") Integer pageNo,
                                              @RequestParam("page-size") Integer pageSize,
                                              @RequestParam("sort-by") String sortBy){
        return queryService.getAllUnResolvedQuery(keyword,pageNo,pageSize,sortBy);
    }

    @GetMapping("get/student-data")
    public ResponseEntity<?> getAllStudentsProfileWhoAskedQuery(@RequestParam("page-no") Integer pageNo,
                                                                @RequestParam("page-size") Integer pageSize,
                                                                @RequestParam("sort-by") String sortBy){
        return queryService.getAllStudentsWhoAskedTheQuery(pageNo,pageSize,sortBy);
    }

    @GetMapping("get/teacher-data")
    public ResponseEntity<?> getAllTeachersWhomQueryHaveBeenAssigned(@RequestParam("page-no") Integer pageNo,
                                                                     @RequestParam("page-size") Integer pageSize,
                                                                     @RequestParam("sort-by") String sortBy){
        return queryService.getAllTeacherWhomQueryHaveBeenAssigned(pageNo,pageSize,sortBy);
    }
}
