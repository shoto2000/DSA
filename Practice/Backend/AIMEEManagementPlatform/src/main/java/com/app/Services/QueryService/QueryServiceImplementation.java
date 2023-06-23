package com.app.Services.QueryService;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.BadRequestException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Helpers.PaginationHelper;
import com.app.Models.Query;
import com.app.Models.Standard;
import com.app.Models.Student;
import com.app.Models.Teacher;
import com.app.Payloads.ApiResponse;
import com.app.Payloads.QueryRequest;
import com.app.Repositories.QueryRepository;
import com.app.Repositories.StudentRepository;
import com.app.Repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QueryServiceImplementation implements QueryService{

    @Autowired
    private QueryRepository queryRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public static String generateUniqueCode() {
        long timestamp = System.currentTimeMillis();
        int random = new Random().nextInt(10000);
        return String.format( "QUERY"+"%d-%04d", timestamp, random);
    }

    @Override
    public ResponseEntity<?> addQuery(QueryRequest request) {
        Optional<Student> foundStudent = studentRepository.findByStudentCode(request.getStudentCode());
        if(foundStudent.isPresent()){
                Query newQuery = new Query();
                newQuery.setQueryCode(generateUniqueCode());
                newQuery.setQueryTitle(request.getQueryTitle());
                newQuery.setQueryDetail(request.getQueryDescription());
                newQuery.setStudentCode(request.getStudentCode());
                LocalDateTime current = LocalDateTime.now();
                newQuery.setCreatedDateTime(current);
                newQuery.setUpdatedDataTime(current);
                newQuery.setResolveStatus(false);
                queryRepository.save(newQuery);
                return new ResponseEntity<>(new ApiResponse(true,"Query added"),HttpStatus.CREATED);
        }
        throw new ResourceNotFoundException("Student not Found");
    }

    @Override
    public ResponseEntity<?> getAllQueryOfStudentByStudentCode(String studentCode,String keyword, Integer pageNo,Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        Optional<Student> foundStudent = studentRepository.findByStudentCode(studentCode);
        if(foundStudent.isPresent()){
            if(keyword.length()==0){
                List<Query> matchingQueries = queryRepository.findAll().stream()
                        .filter(query -> query.getStudentCode().equals(studentCode))
                        .collect(Collectors.toList());
                Page<Query> pageQueries = PaginationHelper.getPage(matchingQueries,pageable);
                return new ResponseEntity<>(pageQueries, HttpStatus.FOUND);
            }
            List<Query> searchedQuery = queryRepository.findByQueryCodeOrQueryTitleContainingIgnoreCaseOrderByQueryTitle(keyword,keyword).stream()
                    .filter(query -> query.getStudentCode().equals(studentCode))
                    .collect(Collectors.toList());
            Page<Query> pageQueries = PaginationHelper.getPage(searchedQuery,pageable);
            return new ResponseEntity<>(pageQueries,HttpStatus.FOUND);
        }
        throw new ResourceNotFoundException("Student not Found");
    }

    @Override
    public ResponseEntity<?> getAllQueryAssignedToTeacherByEmail(String teacherEmail,String keyword, Integer pageNo,Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        Teacher teacher = teacherRepository.findByTeacherEmail(teacherEmail);
        if(teacher!=null){
            if(keyword.length()==0){
                List<Query> matchingQueries = queryRepository.findAll().stream()
                        .filter(query -> {
                            String teacherEmails = query.getTeacherEmail();
                            return teacherEmails != null && teacherEmails.equals(teacherEmail);
                        })
                        .collect(Collectors.toList());
                Page<Query> pageQueries = PaginationHelper.getPage(matchingQueries,pageable);
                return new ResponseEntity<>(pageQueries, HttpStatus.FOUND);
            }
            List<Query> searchedQueries = queryRepository.findByQueryCodeOrQueryTitleContainingIgnoreCaseOrderByQueryTitle(keyword,keyword)
                    .stream()
                    .filter(query -> {
                        String teacherEmails = query.getTeacherEmail();
                        return teacherEmails != null && teacherEmails.equals(teacherEmail);
                    }).collect(Collectors.toList());
            Page<Query> pageQueries = PaginationHelper.getPage(searchedQueries,pageable);
            return new ResponseEntity<>(pageQueries,HttpStatus.FOUND);
        }
        throw new ResourceNotFoundException("Teacher not Found");

    }

    @Override
    public ResponseEntity<?> getAllQuery(String keyword,Integer pageNo,Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        if(keyword.length()==0){
            return new ResponseEntity<>(queryRepository.findAll(pageable),HttpStatus.FOUND);
        }
        List<Query> searchedQueries = queryRepository.findByQueryCodeOrQueryTitleContainingIgnoreCaseOrderByQueryTitle(keyword,keyword);
        Page<Query> pageQueries = PaginationHelper.getPage(searchedQueries,pageable);
        return new ResponseEntity<>(pageQueries,HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> assignQueryToTeacher(String queryCode, String teacherEmail) {
        Optional<Query> existQuery = queryRepository.findByQueryCode(queryCode);
        if(existQuery.isPresent()){
            Teacher teacher = teacherRepository.findByTeacherEmail(teacherEmail);
            if(teacher!=null){
                Query query = existQuery.get();
                query.setTeacherEmail(teacherEmail);
                query.setUpdatedDataTime(LocalDateTime.now());
                queryRepository.save(query);
                return new ResponseEntity<>(new ApiResponse(true,"Teacher Assigned"),HttpStatus.CREATED);
            }
            throw new ResourceNotFoundException("Teacher not Found");
        }
        throw new ResourceNotFoundException("Query not Found");
    }

    @Override
    public ResponseEntity<?> resolveQuery(String queryCode) {
        Optional<Query> existQuery = queryRepository.findByQueryCode(queryCode);
        if(existQuery.isPresent()){
            Query getQuery =existQuery.get();
            if(getQuery.isResolveStatus()==true){
                throw new AlreadyExistsException("Query Already Resolved");
            }
            getQuery.setResolveStatus(true);
            getQuery.setUpdatedDataTime(LocalDateTime.now());
            queryRepository.save(getQuery);
            return new ResponseEntity<>(new ApiResponse(true,"Query Resolved"),HttpStatus.CREATED);
        }
        throw new ResourceNotFoundException("Query not Found");
    }

    @Override
    public ResponseEntity<?> reOpenQuery(String queryCode) {
        Optional<Query> existQuery = queryRepository.findByQueryCode(queryCode);
        if(existQuery.isPresent()){
            Query getQuery = existQuery.get();
            if(getQuery.isResolveStatus()==false){
                throw new AlreadyExistsException("Query is Already Open");
            }
            getQuery.setResolveStatus(false);
            getQuery.setUpdatedDataTime(LocalDateTime.now());
            queryRepository.save(getQuery);
            return new ResponseEntity<>(new ApiResponse(true,"Query Re-opened"),HttpStatus.CREATED);
        }
        throw new ResourceNotFoundException("Query not Found");
    }

    @Override
    public ResponseEntity<?> getAllUnResolvedQuery(String keyword,Integer pageNo,Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        if(keyword.length()==0){
            List<Query> matchingQueries = queryRepository.findAll().stream()
                    .filter(query -> query.isResolveStatus()==false)
                    .collect(Collectors.toList());
            Page<Query> pageQueries = PaginationHelper.getPage(matchingQueries,pageable);
            return new ResponseEntity<>(pageQueries,HttpStatus.FOUND);
        }
        List<Query> searchedQueries = queryRepository.findByQueryCodeOrQueryTitleContainingIgnoreCaseOrderByQueryTitle(keyword,keyword);
        List<Query> unResolvedQueries = searchedQueries.stream().filter(s -> !s.isResolveStatus()).collect(Collectors.toList());
        Page<Query> pageQueries = PaginationHelper.getPage(unResolvedQueries,pageable);
        return new ResponseEntity<>(pageQueries,HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllResolvedQuery(String keyword,Integer pageNo,Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        if(keyword.length()==0){
            List<Query> matchingQueries = queryRepository.findAll().stream()
                    .filter(query -> query.isResolveStatus()==true)
                    .collect(Collectors.toList());
            Page<Query> pageQueries = PaginationHelper.getPage(matchingQueries,pageable);
            return new ResponseEntity<>(pageQueries,HttpStatus.FOUND);
        }
        List<Query> searchedQueries = queryRepository.findByQueryCodeOrQueryTitleContainingIgnoreCaseOrderByQueryTitle(keyword,keyword);
        List<Query> resolvedQueries = searchedQueries.stream().filter(s -> s.isResolveStatus()).collect(Collectors.toList());
        Page<Query> pageQueries = PaginationHelper.getPage(resolvedQueries,pageable);
        return new ResponseEntity<>(pageQueries,HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllStudentsWhoAskedTheQuery(Integer pageNo,Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        Set<String> studentCodesSet = queryRepository.findAll().stream().map(s->s.getStudentCode()).collect(Collectors.toSet());

        List<Student> students = studentCodesSet.stream()
                .map(s -> studentRepository.findByStudentCode(s).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Page<Student> pageStudent = PaginationHelper.getPage(students,pageable);
        return new ResponseEntity<>(pageStudent,HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllTeacherWhomQueryHaveBeenAssigned(Integer pageNo,Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        Set<String> teacherEmailSet = queryRepository.findAll().stream().filter(s -> s.getTeacherEmail() != null).map(s->s.getTeacherEmail()).collect(Collectors.toSet());

        List<Teacher> teachers = teacherEmailSet.stream().map(s->teacherRepository.findByTeacherEmail(s)).collect(Collectors.toList());
        Page<Teacher> pageTeacher = PaginationHelper.getPage(teachers,pageable);
        return new ResponseEntity<>(pageTeacher,HttpStatus.FOUND);
    }

}
