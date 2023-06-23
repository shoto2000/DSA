package com.app.Services.TeacherService;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Exceptions.UserException;
import com.app.Models.*;
import com.app.Payloads.AddUserRequest;
import com.app.Payloads.DeleteResponse;
import com.app.Payloads.TeacherRequest;
import com.app.Repositories.*;
import com.app.Services.TrashBinService.TrashBinService;
import com.app.Services.UserService.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private TrashBinService trashBinService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SubjectRepository subjectRepository;


    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Override
    public ResponseEntity<?> addTeacher(TeacherRequest teacherRequest) {
        logger.info("Setting value in addUserRequest");
        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setName(teacherRequest.getTeacherName());
        addUserRequest.setEmail(teacherRequest.getTeacherEmail());
        addUserRequest.setPassword(teacherRequest.getPassword());
        addUserRequest.setRole(Roles.Teacher);
        addUserRequest.setGender(teacherRequest.getGender());

        logger.info("Finding school by branch code");
        School school = schoolRepository.findByBranchCode(teacherRequest.getBranchCode());
        if (school != null) {
            logger.info("School Found so encoding teacher password");
            teacherRequest.setPassword(passwordEncoder.encode(teacherRequest.getPassword()));
            logger.info("Saving teacher in user table");
            userService.saveUser(addUserRequest, teacherRequest.getBranchCode());
            logger.info("Saving teacher in teacher table");
            School getSchool = schoolRepository.findByBranchCode(teacherRequest.getBranchCode());
            Teacher teacher = new Teacher();
            teacher.setTeacherEmail(teacherRequest.getTeacherEmail());
            teacher.setTeacherName(teacherRequest.getTeacherName());
            teacher.setGender(teacherRequest.getGender());
            teacher.setPassword(teacherRequest.getPassword());
            teacher.setClassTeacherOfStandard(teacherRequest.getClassTeacherOfStandard());
            teacher.setBranchCode(teacherRequest.getBranchCode());
            teacher.setClassTeacherOfStandard(teacherRequest.getClassTeacherOfStandard());
            getSchool.getTeachers().add(teacher);
            schoolRepository.save(getSchool);
            logger.info("Returning teacher response");
            return new ResponseEntity<>(teacher, HttpStatus.CREATED);
        }
        logger.warn("School not found so not creating teacher");
        throw new ResourceNotFoundException("School not found");

    }

    @Override
    public ResponseEntity<?> moveTeacherToTrash(String email) {
        Teacher getTeacher = teacherRepository.findByTeacherEmail(email);
        if (getTeacher != null) {
            School getSchool = schoolRepository.findByBranchCode(getTeacher.getBranchCode());
            if (getSchool != null) {
                getSchool.getTeachers().remove(getTeacher);
                entityManager.detach(getTeacher);
                trashBinService.moveTeacherToTrashBin(getTeacher);
                teacherRepository.delete(getTeacher);
                Optional<User> getUser = userRepository.findByEmail(email);
                if (getUser.isPresent()) {
                    userRepository.delete(getUser.get());
                    return new ResponseEntity<>(new DeleteResponse("Teacher"), HttpStatus.OK);
                }
                throw new UserException("User not Found");
            }
            throw new ResourceNotFoundException("School not Found");
        }
        throw new UserException("Teacher not Found");


    }


    @Override
    public ResponseEntity<?> getTeacher(String email) {
        logger.info("getTeacher invoked for getting teacher and finding teacher by email");
        Teacher teacher = teacherRepository.findByTeacherEmail(email);
        if (teacher != null) {
            logger.info("Teacher Found and returning teacher response");
            return new ResponseEntity<>(teacher, HttpStatus.FOUND);
        }
        logger.warn("Teacher not found try to create a new teacher");
        throw new ResourceNotFoundException("Teacher not found");
    }

    @Override
    public ResponseEntity<?> assignSubjectAndStandardToTeacher(SubjectStandardToTeacher subjectStandardToTeacher, String email) {
        logger.info("assignSubjectAndStandardToTeacher() invoked and finding teacher by email");
        Teacher teacher = teacherRepository.findByTeacherEmail(email);
        if (teacher != null) {
            logger.info("Teacher Found so finding standard ");
            Standard standard = standardRepository.findByStandardLevel(subjectStandardToTeacher.getStandardLevel());
            if (standard != null) {
                logger.info("Standard found so now finding subject");
                Optional<Subject> subject = standard.getSubjects().stream().filter(s -> s.getSubjectCode().equals(subjectStandardToTeacher.getSubjectCode())).findAny();
                if (subject.isPresent()) {
                    logger.info("Subject found so now finding teacher already assigned to that subject and standard or not");
                    Optional<SubjectStandardToTeacher> existsSubject = teacher.getAssignSubjectInAStandard().stream().filter(s -> s.getSubjectCode().equals(subjectStandardToTeacher.getSubjectCode())).findAny();
                    if (!existsSubject.isPresent()) {
                        logger.info("Teacher not yet assigned so assigning teacher to subjec and standard");
                        teacher.getAssignSubjectInAStandard().add(subjectStandardToTeacher);
                        logger.info("Updating teacher");
                        teacherRepository.save(teacher);
                        return new ResponseEntity<>(teacher, HttpStatus.CREATED);
                    }
                    logger.warn("Subject Already Assigned");
                    throw new AlreadyExistsException("Subject Already Assigned");
                }
                logger.warn("Subject Not Found please add subject");
                throw new ResourceNotFoundException("Subject Not found");
            }
            logger.warn("Standard not found please add standard");
            throw new ResourceNotFoundException("Standard not found");

        }
        logger.warn("Teacher not found");
        throw new ResourceNotFoundException("Teacher not found");
    }

    @Override
    public ResponseEntity<?> assignSubjectsAndStandardsToTeacher(Set<SubjectStandardToTeacher> subjectStandardToTeachers, String email) {
        Teacher teacher = teacherRepository.findByTeacherEmail(email);
        if (teacher != null) {
            teacher.getAssignSubjectInAStandard().addAll(subjectStandardToTeachers);
            teacherRepository.save(teacher);
            return new ResponseEntity<>(teacher, HttpStatus.CREATED);
        }
        throw new ResourceNotFoundException("Teacher not found");
    }

    @Override
    public ResponseEntity<?> assignTeacherAsAClassTeacher(String email, String standardLevel) {
        logger.info("assignTeacherAsAClassTeacher() invoked for assinging teacher and finding teacher by email");
        Teacher teacher = teacherRepository.findByTeacherEmail(email);
        if (teacher != null) {
            logger.info("Teacher found and checking already assigned as a class teacher or not or another classTeacher assigned as a class teacher or not");
            if (teacher.getClassTeacherOfStandard() == null) {
                logger.info("None of teacher assigned yet so finding standard");
                Standard standard = standardRepository.findByStandardLevel(standardLevel);
                if (standard != null) {
                    logger.info("Standard found");
                    if (standard.getClassTeacher() == null) {
                        logger.info("Setting classteacher in standard");
                        standard.setClassTeacher(teacher);
                        standardRepository.save(standard);
                        teacher.setClassTeacherOfStandard(standardLevel);
                        teacherRepository.save(teacher);
                        logger.info("Class Teacher Assigned and returning teacher response");
                        return new ResponseEntity<>(teacher, HttpStatus.ACCEPTED);

                    }
                    logger.warn("Class Teacher Already exists as a classTeacher in this standard");
                    throw new AlreadyExistsException("Class Teacher Already Exists");
                }
                logger.info("Standard not found");
                throw new ResourceNotFoundException("Standard not found");

            }
            logger.warn("Teacher Already assigned as a class teacher");
            throw new AlreadyExistsException("Class Teacher Already Exists try to update");
        }
        logger.warn("Teacher not found with this email");
        throw new ResourceNotFoundException("Teacher not found");
    }

    @Override
    public ResponseEntity<?> updateClassTeacher(String email, String standardLevel) {
        logger.info("updateClassTeacher() invoked for assigning new class teacher and finding teacher with email: {}", email);
        Teacher teacher = teacherRepository.findByTeacherEmail(email);
        if (teacher != null) {
            logger.info("Teacher Found and checking any other teacher assigned or not");
            if (teacher.getClassTeacherOfStandard() != null) {
                teacher.setClassTeacherOfStandard(standardLevel);
                teacherRepository.save(teacher);
                logger.info("New teacher assinged and returning teacher response");
                return new ResponseEntity<>(teacher, HttpStatus.ACCEPTED);
            }
            logger.warn("Any class Teacher not added yet");
            throw new ResourceNotFoundException("Class Teacher Not Added Yet");
        }
        logger.warn("Teacher not found with email: {}", email);
        throw new ResourceNotFoundException("Teacher not found");
    }

    @Override
    public ResponseEntity<?> getAllTeacher(String branchCode, Integer pageNo, Integer pageSize, String name) {
        logger.info("Finding teacher in school having branchCode");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        List<Teacher> teachers = teacherRepository.findAll(pageable).stream().filter(t -> t.getBranchCode().equals(branchCode)).collect(Collectors.toList());
        return new ResponseEntity<>(teachers, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllTeacherAvailable(Integer pageNo, Integer pageSize, String name) {
        logger.info("Getting all teacher");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        return new ResponseEntity<>(teacherRepository.findAll(pageable),HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllTheSubjectsAssignedToTeacher(String email) {
        Teacher teacher = teacherRepository.findByTeacherEmail(email);
        if(teacher!=null){
            Set<String> subjectCodes = teacher.getAssignSubjectInAStandard().stream().map(s -> s.getSubjectCode()).collect(Collectors.toSet());
            List<Subject> subjects = subjectCodes.stream()
                    .map(s-> subjectRepository.findBySubjectCode(s))
                    .collect(Collectors.toList());
            if(subjects.size()!=0){
                return new ResponseEntity<>(subjects,HttpStatus.FOUND);
            }
            throw new ResourceNotFoundException("No Subjects assigned to this Teacher");
        }
        throw new ResourceNotFoundException("Teacher not Found");
    }

    @Override
    public ResponseEntity<?> getAllTheStandardsAssignedToTeacher(String email) {
        Teacher teacher = teacherRepository.findByTeacherEmail(email);
        if(teacher!=null){
            Set<String> standardLevel = teacher.getAssignSubjectInAStandard().stream().map(s->s.getStandardLevel()).collect(Collectors.toSet());
            schoolRepository.findByBranchCode(teacher.getBranchCode());
            List<Standard> standards = standardLevel.stream()
                    .map(s -> standardRepository.findByStandardLevel(s))
                    .collect(Collectors.toList());
            if(standards.size()!=0){
                return new ResponseEntity<>(standards,HttpStatus.FOUND);
            }
            throw new ResourceNotFoundException("No Standard assigned to this Teacher");
        }
        throw new ResourceNotFoundException("Teacher not Found");
    }
}
