package com.app.Services.StudentService;

import com.app.Exceptions.AlreadyExistsException;


import com.app.Exceptions.ResourceNotFoundException;
import com.app.Exceptions.UserException;
import com.app.Helpers.FindStandardHelper;
import com.app.Helpers.PaginationHelper;
import com.app.Models.*;
import com.app.Payloads.*;
import com.app.Repositories.StandardRepository;
import com.app.Repositories.StudentRepository;
import com.app.Helpers.CSVHelper;
import com.app.Helpers.FindStandardHelper;
import com.app.Repositories.SubjectRepository;
import com.app.Repositories.StandardRepository;
import com.app.Repositories.StudentRepository;
import com.app.Repositories.StudentTrashBinRepository;
import com.app.Services.TrashBinService.TrashBinService;
import com.app.Services.UserService.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private UserService userService;

    @Autowired
    private FindStandardHelper findStandardOOps;

    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CSVHelper csvHelper;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentTrashBinRepository studentTrashBinRepository;

    @Autowired
    private TrashBinService trashBinService;

    @Autowired
    private FindStandardHelper findStandardHelper;

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);


    @Override
    public ResponseEntity<?> addStudent(Student student, String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("addStudent invoked for adding student");
        logger.info("Finding standard using findStandardHelper");
        Standard standard = findStandardOOps.findStandard(standardLevel, board, yearFrom, branchCode);
        if (standard != null) {
            logger.info("Standard Found and finding student in the Repository");
            Optional<Student> existsStudent = studentRepository.findByStudentCode(student.getStudentCode());
            if (!existsStudent.isPresent()) {
                logger.info("Student not found so increasing no of students in a standard");
                standard.setNoOfStudents(standard.getNoOfStudents() + 1);
                student.setBranchCode(branchCode);
                student.setYearFrom(yearFrom);
                student.setBoard(board);
                student.setStandard(standardLevel);
                student.setStudentGeneralProfile(new StudentGeneralProfile(student.getStudentCode()));
                student.setStudentSportsProfile(new StudentSportsProfile(student.getStudentCode()));
                student.setStudentBehaviouralProfile(new StudentBehaviouralProfile(student.getStudentCode()));
                student.setStudentCoCurriculumActivityProfile(new StudentCoCurriculumActivityProfile(student.getStudentCode()));
                logger.info("Adding student");
                standard.getStudents().add(student);
                logger.info("Saving standard and returning");
                return new ResponseEntity<>(standardRepository.save(standard), HttpStatus.CREATED);
            }
            logger.warn("Student Already Found");
            throw new AlreadyExistsException("Student Already Exists");
        }
        logger.warn("Standard Not Found");
        throw new ResourceNotFoundException("Standard Not Found");
    }

    @Override
    public ResponseEntity<?> getStudentByCode(String searchTerm) {
        logger.info("getStudentByCode invoked for searching student using student code or name");
        List<Student> students = studentRepository.findByStudentCodeOrStudentNameContainingIgnoreCaseOrderByStudentName(searchTerm, searchTerm);
        return new ResponseEntity<>(students, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllStudentsByStandard(String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("getAllStudentByStandard invoked for searching student in standard");
        Standard standard = findStandardOOps.findStandard(standardLevel, board, yearFrom, branchCode);
        List<Student> students = standard.getStudents();
        return new ResponseEntity<>(students, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllStudents(Integer pageNo, Integer pageSize, String name) {
        logger.info("getAllStudents for finding all student in database");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        return new ResponseEntity<>(studentRepository.findAll(pageable), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> moveStudentToTrash(String studentCode) {
        Optional<Student> foundStudent = studentRepository.findByStudentCode(studentCode);
        if(studentTrashBinRepository.findByStudentCode(studentCode).isPresent() && studentRepository.findByStudentCode(studentCode).isPresent()){
            studentTrashBinRepository.delete(studentTrashBinRepository.findByStudentCode(studentCode).get());
        }
        if (foundStudent.isPresent()) {
            Student student = foundStudent.get();
            Standard foundStandard = findStandardHelper.findStandard(student.getStandard(),student.getBoard(),student.getYearFrom(),student.getBranchCode());
            trashBinService.moveStudentToTrashBin(student);
            foundStandard.getStudents().remove(student);
            studentRepository.delete(student);
            foundStandard.setNoOfStudents(foundStandard.getNoOfStudents()-1);
            standardRepository.save(foundStandard);
            return new ResponseEntity<>(new DeleteResponse("Student"),HttpStatus.ACCEPTED);
        }
        throw new ResourceNotFoundException("Student not Found");
    }

    @Override
    public ResponseEntity<?> addSubjectToStudent(String subjectCode, String studentCode) {
        Optional<Student> existStudent = studentRepository.findByStudentCode(studentCode);
        Subject subject = subjectRepository.findBySubjectCode(subjectCode);
        if(!existStudent.isPresent()){
            throw new ResourceNotFoundException("Student not Found");
        } else if(subject==null){
            throw new ResourceNotFoundException("Subject not Found");
        } else{
            Student student = existStudent.get();
            Optional<Subject> existSubject = student.getSubjects().stream().filter(s -> s.getSubjectCode().equals(subjectCode)).findAny();
            if(!existSubject.isPresent()){
                student.getSubjects().add(subject);
                subject.getStudents().add(student);

                studentRepository.save(student);

                return new ResponseEntity<>(new ApiResponse(true,"Subject Assigned to Student"),HttpStatus.CREATED);
            }
            throw new AlreadyExistsException("Subject Already Present");

        }
    }

    @Override
    public ResponseEntity<?> removeSubjectFromStudent(String subjectCode, String studentCode) {
        Optional<Student> existStudent = studentRepository.findByStudentCode(studentCode);
        Subject subject = subjectRepository.findBySubjectCode(subjectCode);
        if(!existStudent.isPresent()){
            throw new ResourceNotFoundException("Student not Found");
        }
        else if(subject==null){
            throw new ResourceNotFoundException("Subject not Found");
        }
        else{
            Student student = existStudent.get();
            Optional<Subject> existSubject = student.getSubjects().stream().filter(s -> s.getSubjectCode().equals(subjectCode)).findAny();
            if(existSubject.isPresent()){
                student.getSubjects().remove(subject);
                subject.getStudents().remove(student);

                studentRepository.save(student);

                return new ResponseEntity<>(new RemoveResponse("Subject"),HttpStatus.ACCEPTED);
            }
            throw new ResourceNotFoundException("Subject not Present");

        }
    }

    @Override
    public ResponseEntity<?> getSubjectsByStudentCode(String studentCode) {
        Optional<Student> existStudent = studentRepository.findByStudentCode(studentCode);
        if(existStudent.isPresent()){
            return new ResponseEntity<>(existStudent.get().getSubjects(),HttpStatus.FOUND);
        }
        throw new ResourceNotFoundException("Student not Found");
    }

    @Override
    public ResponseEntity<?> getStudentsBySubjectCode(String subjectCode) {
        Subject subject = subjectRepository.findBySubjectCode(subjectCode);
        if(subject!=null){
            return new ResponseEntity<>(subject.getStudents(),HttpStatus.FOUND);
        }
        throw new ResourceNotFoundException("Subject not Found");
    }

    @Override
    public ResponseEntity<?> filterStudent(FilterRequest filterRequest, Integer pageNo, Integer pageSize, String name) {
        System.out.println("BranchCode: "+filterRequest.getBranchCode()+" Year From: "+filterRequest.getYearFrom()+" Board: "+filterRequest.getBoard()+" Standard: "+filterRequest.getStandard());
        Pageable pageable = PageRequest.of(pageNo-1,pageSize,Sort.by(name));
        if(filterRequest.getBranchCode()!=null && filterRequest.getYearFrom()==null && filterRequest.getBoard()==null && filterRequest.getStandard()==null )
        {
            return new ResponseEntity<>(studentRepository.findAllByBranchCode(filterRequest.getBranchCode(),pageable),HttpStatus.FOUND);
        }
        else if(filterRequest.getBranchCode()!=null && filterRequest.getYearFrom()!=null && filterRequest.getBoard()==null && filterRequest.getStandard()==null)
        {
            return new ResponseEntity<>(studentRepository.findAllByBranchCodeAndYearFrom(filterRequest.getBranchCode(),filterRequest.getYearFrom(),pageable),HttpStatus.FOUND);
        }
        else if(filterRequest.getBranchCode()!=null && filterRequest.getYearFrom()!=null && filterRequest.getBoard()!=null && filterRequest.getStandard()==null)
        {
            return new ResponseEntity<>(studentRepository.findAllByBranchCodeAndYearFromAndBoard(filterRequest.getBranchCode(),filterRequest.getYearFrom(),filterRequest.getBoard(),pageable),HttpStatus.FOUND);
        }
        else if(filterRequest.getBranchCode()!=null && filterRequest.getYearFrom()!=null && filterRequest.getBoard()!=null && filterRequest.getStandard()!=null)
        {
            return new ResponseEntity<>(studentRepository.findAllByBranchCodeAndYearFromAndBoardAndStandard(filterRequest.getBranchCode(),filterRequest.getYearFrom(),filterRequest.getBoard(),filterRequest.getStandard(),pageable),HttpStatus.FOUND);
        }
        else return new ResponseEntity<>(studentRepository.findAll(pageable),HttpStatus.FOUND);
    }

    @Override
    public List<?> uploadCsvFile(MultipartFile file,String standardLevel, Boards board, Year yearFrom, String branchCode) {

        try {
            // Check if the uploaded file is empty
            if (file == null) {
                throw new IllegalArgumentException("Uploaded file is empty");
            }

            // Check if the uploaded file is a CSV file
            if (!CSVHelper.hasCSVFormat(file)) {
                throw new IllegalArgumentException("Uploaded file is not a CSV file");
            }

            // Convert the CSV file to a list of Student objects
            List<Student> students = csvHelper.csvToStudents(file.getInputStream(),standardLevel, board, yearFrom, branchCode);

            // Check if the list of students is empty
            if (students == null) {
                throw new IllegalArgumentException("No data found in uploaded file");
            }

            // Save the list of students to the database and return the saved entities
            return students;

        } catch (IOException e) {
            throw new RuntimeException("Failed to process CSV file: " + e.getMessage());
        }

    }

    
    @Override
    public ResponseEntity<?> downloadCsvFile(String standardLevel, Boards board, Year yearFrom, String branchCode) {
        List<Student> students = studentRepository.findAll();

        ByteArrayInputStream input = CSVHelper.studentsToCSV(students);
        InputStreamResource file = new InputStreamResource(input);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/csv"));
        headers.setContentDispositionFormData("attachment", "students");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment:students")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }



 }
