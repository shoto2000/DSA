package com.app.Services.TrashBinService;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Exceptions.UserException;
import com.app.Helpers.FindAcademicYearHelper;
import com.app.Helpers.FindBoardHelper;
import com.app.Helpers.FindStandardHelper;
import com.app.Models.*;
import com.app.Payloads.DeleteResponse;
import com.app.Payloads.MoveToTrashResponse;
import com.app.Payloads.UndoResponse;
import com.app.Repositories.*;
import com.app.Services.SchoolService.SchoolService;
import com.app.Services.UserService.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrashBinServiceImpl implements TrashBinService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private AcademicYearTrashBinRepository academicYearTrashBinRepository;

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Autowired
    private FindAcademicYearHelper findAcademicYearHelper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardTrashRepository boardTrashRepository;

    @Autowired
    private StandardTrashRepository standardTrashRepository;

    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private FindBoardHelper findBoardHelper;

    @Autowired
    private UserTrashBinRepository userTrashBinRepository;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private TeacherTrashBinRepository teacherTrashBinRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectTrashBinRepository subjectTrashBinRepository;

    @Autowired
    private FindStandardHelper findStandardHelper;

    @Autowired
    private StudentTrashBinRepository studentTrashBinRepository;

    private static final Logger logger = LoggerFactory.getLogger(TrashBinServiceImpl.class);

    @Override
    public void moveAcademicYearToTrashBin(String branchCode, AcademicYear academicYear) {
        logger.info("moveAcademicYearToTrash() invoked for adding academic year to trash");
        AcademicYearTrashBin academicYearTrashBin = new AcademicYearTrashBin();
        academicYearTrashBin.setBranchCode(branchCode);
        academicYearTrashBin.setAcademicYears(academicYear);
        academicYearTrashBinRepository.save(academicYearTrashBin);
        logger.info("academic year added successfully");
    }

    @Override
    public ResponseEntity<?> undoAcademicYearFromTrashBin(Year yearFrom) {
        logger.info("undoAcademicYearTrashBin() invoked for undoing the trash and now finding academic year");
        AcademicYearTrashBin academicYearTrashBin = academicYearTrashBinRepository.findByAcademicYearsAcademicYearFrom(yearFrom);
        logger.info("Finding school");
        School school = schoolRepository.findByBranchCode(academicYearTrashBin.getBranchCode());
        if (school != null) {
            logger.info("School Found and adding academic year in school");
            school.getAcademicYears().add(academicYearTrashBin.getAcademicYears());
            schoolRepository.save(school);
            academicYearTrashBinRepository.delete(academicYearTrashBin);
            logger.info("Academic year moved to school");
            return new ResponseEntity<>(new UndoResponse("Academic Year"), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("School Not Found");

    }

    @Override
    public ResponseEntity<List<AcademicYear>> getAllAcademicYearsInTrash() {
        List<AcademicYearTrashBin> academicYearTrashBins = academicYearTrashBinRepository.findAll();
        List<AcademicYear> academicYears = new ArrayList<>();
        for (AcademicYearTrashBin a : academicYearTrashBins) {
            academicYears.add(a.getAcademicYears());
        }
        return new ResponseEntity<>(academicYears, HttpStatus.FOUND);

    }

    @Override
    public ResponseEntity<?> deleteAcademicYearPermanently(Year yearFrom) {
        logger.info("deleteAcademicYearPermanently() invoked for deleting academic year permanently from the trash and now finding academic year");
        AcademicYearTrashBin academicYearTrashBin = academicYearTrashBinRepository.findByAcademicYearsAcademicYearFrom(yearFrom);
        if (academicYearTrashBin != null) {
            logger.info("Academic year found now deleteing permanently");
            academicYearTrashBinRepository.delete(academicYearTrashBin);
            academicYearRepository.delete(academicYearTrashBin.getAcademicYears());
            logger.info("Academic year deleted for permanently");
            return new ResponseEntity<>(new DeleteResponse("Academic Year"), HttpStatus.OK);
        }
        logger.warn("Academic Year Not Exists In Trash");
        throw new ResourceNotFoundException("Academic Year Not Found In Trash");
    }

    @Override
    public ResponseEntity<?> deleteAllAcademicYearsPermanently() {
        logger.info("deleteAllAcademicYearsPermanently() invoked for delteing all standards");
        List<AcademicYearTrashBin> academicYearTrashBins = academicYearTrashBinRepository.findAll();
        if (!academicYearTrashBins.isEmpty()) {
            List<AcademicYear> academicYears = getAllAcademicYearsInTrash().getBody();
            academicYearTrashBinRepository.deleteAll(academicYearTrashBins);
            academicYearRepository.deleteAll(academicYears);
            logger.info("All academic years deleted permanently");
            return new ResponseEntity<>(new DeleteResponse("All Academic Years"), HttpStatus.OK);
        }
        logger.info("Academic Year Trash is Empty");
        throw new ResourceNotFoundException("Trash is empty");
    }

    @Override
    public ResponseEntity<?> moveBoardToTrashBin(String branchCode, Year yearFrom, Boards board) {
        logger.info("moveBoardToTrashBin() invoked for moving board to trash bin and finding board using findBoardHelper");
        SchoolBoard schoolBoard = findBoardHelper.findBoard(board, yearFrom, branchCode);
        if (schoolBoard != null) {
            logger.info("School board found and now moving to board trash bin");
            BoardTrashBin boardTrashBin = new BoardTrashBin();
            boardTrashBin.setBranchCode(branchCode);
            boardTrashBin.setAcademicYearFrom(yearFrom);
            boardTrashBin.setSchoolBoard(schoolBoard);
            boardTrashRepository.save(boardTrashBin);
            AcademicYear academicYear = academicYearRepository.findByAcademicYearFrom(yearFrom);
            academicYear.getBoards().remove(schoolBoard);
            academicYearRepository.save(academicYear);
            logger.info("School board moved to trash successfully");
            return new ResponseEntity<>(new MoveToTrashResponse("Board"), HttpStatus.OK);
        }
        logger.warn("School board not found in acadmic year");
        throw new ResourceNotFoundException("School Board Not Found");
    }

    @Override
    public ResponseEntity<?> undoBoardFromTrashBin(Boards board) {
        logger.info("undoBoardFromTrashBin() invoked for undoing board from trashboard and finding board in trash");
        BoardTrashBin boardTrashBin = boardTrashRepository.findBySchoolBoardBoard(board);
        if (boardTrashBin != null) {
            logger.info("Board found in trash and moving to academic year");
            AcademicYear academicYear = findAcademicYearHelper.findAcademicYear(boardTrashBin.getAcademicYearFrom(), boardTrashBin.getBranchCode());
            academicYear.getBoards().add(boardTrashBin.getSchoolBoard());
            academicYearRepository.save(academicYear);
            boardTrashRepository.delete(boardTrashBin);
            logger.info("Board successfully moved from trash");
            return new ResponseEntity<>(new UndoResponse("Board"), HttpStatus.OK);

        }
        logger.warn("Board not found in trash");
        throw new ResourceNotFoundException("Board not found in trash");
    }

    @Override
    public ResponseEntity<List<SchoolBoard>> getAllBoardInTrash() {
        logger.info("getAllBoardInTrash() invoked for finding all boards in trash");
        List<BoardTrashBin> boardTrashBins = boardTrashRepository.findAll();
        List<SchoolBoard> schoolBoards = new ArrayList<>();
        for (BoardTrashBin b : boardTrashBins) {
            schoolBoards.add(b.getSchoolBoard());
        }
        return new ResponseEntity<>(schoolBoards, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> deleteBoardPermanently(Boards board) {
        logger.info("deleteBoardPermanently() invoked for deleting board permanently");
        BoardTrashBin boardTrashBin = boardTrashRepository.findBySchoolBoardBoard(board);
        if (boardTrashBin != null) {
            logger.info("School Board Found and deleting permanently");
            SchoolBoard schoolBoard = boardTrashBin.getSchoolBoard();
            boardTrashRepository.delete(boardTrashBin);
            boardRepository.delete(schoolBoard);
            logger.info("School Board Permanently Deleted");
            return new ResponseEntity<>(new DeleteResponse("Board"), HttpStatus.OK);
        }
        logger.warn("Board not found in trash");
        throw new ResourceNotFoundException("Board not found in trash");
    }

    @Override
    public ResponseEntity<?> deleteAllBoardsPermanently() {
        logger.info("deleteAllBoardsPermanently() invoked for deleteing all boards permanently");
        List<BoardTrashBin> boardTrashBins = boardTrashRepository.findAll();
        if (!boardTrashBins.isEmpty()) {
            List<SchoolBoard> schoolBoards = getAllBoardInTrash().getBody();
            boardTrashRepository.deleteAll();
            boardRepository.deleteAll(schoolBoards);
            return new ResponseEntity<>(new DeleteResponse("All Boards"), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Boards not found in trash");
    }

    @Override
    public void moveStandardToTrashBin(String branchCode, Year yearFrom, Boards board, Standard standard) {
        logger.info("moveStandardToTrash() invoked for moving standard to trash");
        StandardTrashBin standardTrashBin = new StandardTrashBin();
        standardTrashBin.setBranchCode(branchCode);
        standardTrashBin.setAcademicYearFrom(yearFrom);
        standardTrashBin.setBoard(board);
        standardTrashBin.setStandard(standard);
        standardTrashRepository.save(standardTrashBin);
        logger.info("Standard moved to trash successfully");
    }

    @Override
    public ResponseEntity<?> undoStandardFromTrashBin(String standard) {
        logger.info("undoStandardFromTrashBin() invoked for undoing standard from trash and now finding standard in trash");
        StandardTrashBin standardTrashBin = standardTrashRepository.findByStandardStandardLevel(standard);
        if (standardTrashBin != null) {
            logger.info("Standard Found so now finding school board");
            SchoolBoard schoolBoard = findBoardHelper.findBoard(standardTrashBin.getBoard(), standardTrashBin.getAcademicYearFrom(), standardTrashBin.getBranchCode());
            if (schoolBoard != null) {
                logger.info("School Board Found and now adding standard");
                schoolBoard.getStandards().add(standardTrashBin.getStandard());
                boardRepository.save(schoolBoard);
                standardTrashRepository.delete(standardTrashBin);
                logger.info("Standard successfully moved to the board");
                return new ResponseEntity<>(new UndoResponse("Standard"), HttpStatus.OK);
            }
            logger.warn("School Board Not Found ");
            throw new ResourceNotFoundException("School Board Not Found");
        }
        logger.warn("Standard Not Found");
        throw new ResourceNotFoundException("Standard Not Found In Trash");
    }

    @Override
    public ResponseEntity<List<Standard>> getAllStandardInTrashBin() {
        logger.info("getAllStandardInTrashBin() invoked for getting standards");
        List<StandardTrashBin> standardTrashBins = standardTrashRepository.findAll();
        List<Standard> standards = new ArrayList<>();
        for (StandardTrashBin s : standardTrashBins) {
            standards.add(s.getStandard());
        }
        return new ResponseEntity<>(standards, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteStandardPermanently(String standard) {
        logger.info("deleteStandardPermanently() invoked for undoing standard from trash and now finding standard in trash");
        StandardTrashBin standardTrashBin = standardTrashRepository.findByStandardStandardLevel(standard);
        if (standardTrashBin != null) {
            logger.info("Standard found and now deleteing permanently");
            standardTrashRepository.delete(standardTrashBin);
            standardRepository.delete(standardTrashBin.getStandard());
            logger.info("Standard deleted successfully");
            return new ResponseEntity<>(new DeleteResponse("Standard"), HttpStatus.OK);
        }
        logger.warn("Standard Not Found");
        throw new ResourceNotFoundException("Standard Not Found In Trash");
    }

    @Override
    public ResponseEntity<?> deleteAllStandardsPermanently() {
        logger.info("deleteAllStandardsPermanently() invoked and finding standards");
        List<StandardTrashBin> standardTrashBins = standardTrashRepository.findAll();
        if (!standardTrashBins.isEmpty()) {
            logger.info("Standards found");
            List<Standard> standards = getAllStandardInTrashBin().getBody();
            standardTrashRepository.deleteAll();
            standardRepository.deleteAll(standards);
            return new ResponseEntity<>(new DeleteResponse("All Standard"), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Standards Not Found");
    }

    @Override
    public void moveUserToTrashBin(User user) {
        logger.info("moveUserToTrash has been Invoked");
        UserTrashBin trashUser = new UserTrashBin();
        trashUser.setName(user.getName());
        trashUser.setPassword(user.getPassword());
        trashUser.setRole(user.getRole());
        trashUser.setImage(user.getImage());
        trashUser.setEmail(user.getEmail());
        trashUser.setGenders(user.getGender());
        trashUser.setBranchName(user.getBranchCode());
        userTrashBinRepository.save(trashUser);
    }

    @Override
    public void moveTeacherToTrashBin(Teacher teacher) {
        TeacherTrashBin trashBin = new TeacherTrashBin();
        trashBin.setTeacherName(teacher.getTeacherName());
        trashBin.setTeacherEmail(teacher.getTeacherEmail());
        trashBin.setGender(teacher.getGender());
        trashBin.setPassword(teacher.getPassword());
        trashBin.setBranchCode(teacher.getBranchCode());
        trashBin.setClassTeacherOfStandard(teacher.getClassTeacherOfStandard());
        trashBin.setAssignSubjectInAStandard(teacher.getAssignSubjectInAStandard());
        teacherTrashBinRepository.save(trashBin);
    }

    @Override
    public void moveStudentToTrashBin(Student student) {
        StudentTrashBin trashBin = new StudentTrashBin();
        trashBin.setStudentCode(student.getStudentCode());
        trashBin.setStudentName(student.getStudentName());
        trashBin.setBranchCode(student.getBranchCode());
        trashBin.setYearFrom(student.getYearFrom());
        trashBin.setBoard(student.getBoard());
        trashBin.setStandard(student.getStandard());
        trashBin.setStudentGeneralProfile(student.getStudentGeneralProfile());
        trashBin.setStudentBehaviouralProfile(student.getStudentBehaviouralProfile());
        trashBin.setStudentSportsProfile(student.getStudentSportsProfile());
        trashBin.setStudentCoCurriculumActivityProfile(student.getStudentCoCurriculumActivityProfile());
        studentTrashBinRepository.save(trashBin);
    }


    @Override
    public ResponseEntity<?> undoUserFromTrash(String email) {
        Optional<UserTrashBin> existUserTrash = userTrashBinRepository.findByEmail(email);

        if (existUserTrash.isPresent()) {
            UserTrashBin trashUser = existUserTrash.get();
            User newUser = new User();
            newUser.setName(trashUser.getName());
            newUser.setPassword(trashUser.getPassword());
            newUser.setRole(trashUser.getRole());
            newUser.setImage(trashUser.getImage());
            newUser.setEmail(trashUser.getEmail());
            newUser.setGender(trashUser.getGenders());
            newUser.setBranchCode(trashUser.getBranchName());

            if (trashUser.getRole().equals(Roles.Principal)) {
                schoolService.assignPrincipal(trashUser.getBranchName(), newUser);
            } else if (trashUser.getRole().equals(Roles.Director)) {
                schoolService.assignDirector(trashUser.getBranchName(), newUser);
            } else if (trashUser.getRole().equals(Roles.AcademicAuditor)) {
                schoolService.addAcademicAuditor(trashUser.getBranchName(), newUser);
            } else if (trashUser.getRole().equals(Roles.AdminStaff)) {
                schoolService.addAdminStaff(trashUser.getBranchName(), newUser);
            }
            userTrashBinRepository.delete(trashUser);
            return new ResponseEntity<>(new UndoResponse("User"), HttpStatus.OK);

        }
        throw new UserException("User not Found in Trash");
    }

    @Override
    public ResponseEntity<?> undoTeacherFromTrashBin(String email) {
        Optional<TeacherTrashBin> existTrashBin = teacherTrashBinRepository.findByTeacherEmail(email);

        if (existTrashBin.isPresent()) {
            TeacherTrashBin trashBin = existTrashBin.get();
            entityManager.detach(trashBin);
            Teacher teacher = new Teacher();
            teacher.setTeacherName(trashBin.getTeacherName());
            teacher.setTeacherEmail(trashBin.getTeacherEmail());
            teacher.setGender(trashBin.getGender());
            teacher.setPassword(trashBin.getPassword());
            teacher.setBranchCode(trashBin.getBranchCode());
            teacher.setClassTeacherOfStandard(trashBin.getClassTeacherOfStandard());
            teacher.setAssignSubjectInAStandard(trashBin.getAssignSubjectInAStandard());

            logger.info("Finding school by branch code");
            School school = schoolRepository.findByBranchCode(teacher.getBranchCode());
            if (school != null) {
                logger.info("Saving teacher in user table");
                Optional<User> existsUser = userRepository.findByEmail(teacher.getTeacherEmail());
                if (!existsUser.isPresent()) {
                    User newUser = new User();
                    newUser.setName(teacher.getTeacherName());
                    newUser.setEmail(teacher.getTeacherEmail());
                    newUser.setRole(Roles.Teacher);
                    newUser.setPassword(teacher.getPassword());
                    newUser.setGender(teacher.getGender());

                    userRepository.save(newUser);
                } else {
                    throw new UserException("User already present");
                }
                logger.info("Saving teacher in teacher table");
                School getSchool = schoolRepository.findByBranchCode(teacher.getBranchCode());
                getSchool.getTeachers().add(teacher);
                schoolRepository.save(getSchool);
                teacherTrashBinRepository.delete(trashBin);
                logger.info("Returning teacher response");
                return new ResponseEntity<>(new UndoResponse("Teacher"), HttpStatus.CREATED);
            }
            logger.warn("School not found so not creating teacher");
            throw new ResourceNotFoundException("School not found");

        }
        throw new UserException("Teacher not found in Trash");
    }

    @Override
    public ResponseEntity<?> undoStudentFromTrashBin(String studentCode) {
        Optional<StudentTrashBin> existsTrashBin = studentTrashBinRepository.findByStudentCode(studentCode);
        if (existsTrashBin.isPresent()) {
            StudentTrashBin trashBin = existsTrashBin.get();
            Standard foundStandard = findStandardHelper.findStandard(trashBin.getStandard(), trashBin.getBoard(), trashBin.getYearFrom(), trashBin.getBranchCode());
            if (foundStandard != null) {
                logger.info("Standard Found and finding student in that standard");
                Optional<Student> existsStudent = foundStandard.getStudents().stream().filter(s -> s.getStudentCode().equals(trashBin.getStudentCode())).findAny();
                if (!existsStudent.isPresent()) {
                    logger.info("Student not found so increasing no of students in a standard");
                    foundStandard.setNoOfStudents(foundStandard.getNoOfStudents() + 1);
                    Student student = new Student();
                    student.setStudentCode(studentCode);
                    student.setStudentName(trashBin.getStudentName());
                    student.setBranchCode(trashBin.getBranchCode());
                    student.setYearFrom(trashBin.getYearFrom());
                    student.setBoard(trashBin.getBoard());
                    student.setStandard(trashBin.getStandard());
                    student.setStudentGeneralProfile(trashBin.getStudentGeneralProfile());
                    student.setStudentSportsProfile(trashBin.getStudentSportsProfile());
                    student.setStudentBehaviouralProfile(trashBin.getStudentBehaviouralProfile());
                    student.setStudentCoCurriculumActivityProfile(trashBin.getStudentCoCurriculumActivityProfile());
                    logger.info("Adding student");
                    foundStandard.getStudents().add(student);
                    standardRepository.save(foundStandard);
                    studentTrashBinRepository.delete(trashBin);
                    logger.info("Saving standard and returning");
                    return new ResponseEntity<>(new UndoResponse("Student"), HttpStatus.CREATED);
                }
                logger.warn("Student Already Found");
                throw new AlreadyExistsException("Student Already Exists");
            }
            throw new ResourceNotFoundException("Standard not Found");
        }
        throw new ResourceNotFoundException("Student not found in Trash");
    }

    @Override
    public ResponseEntity<?> deleteUserFromTrash(String email) {
        Optional<UserTrashBin> existUserTrash = userTrashBinRepository.findByEmail(email);

        if (existUserTrash.isPresent()) {
            UserTrashBin trashUser = existUserTrash.get();
            userTrashBinRepository.delete(trashUser);
            return new ResponseEntity<>(new DeleteResponse("User"), HttpStatus.ACCEPTED);
        }
        throw new UserException("User not Found in Trash");
    }

    @Override
    public ResponseEntity<?> deleteTeacherFromTrash(String email) {
        Optional<TeacherTrashBin> existTrashBin = teacherTrashBinRepository.findByTeacherEmail(email);

        if(existTrashBin.isPresent()){
            TeacherTrashBin trashBin = existTrashBin.get();
            teacherTrashBinRepository.delete(trashBin);
            return new ResponseEntity<>(new DeleteResponse("Teacher"),HttpStatus.ACCEPTED);
        }
        throw new UserException("Teacher not Found in Trash");
    }

    @Override
    public ResponseEntity<?> deleteStudentFromTrash(String studentCode) {
        Optional<StudentTrashBin> existsTrashBin = studentTrashBinRepository.findByStudentCode(studentCode);

        if(existsTrashBin.isPresent()){
            StudentTrashBin trashBin = existsTrashBin.get();
            studentTrashBinRepository.delete(trashBin);
            return new ResponseEntity<>(new DeleteResponse("Student"),HttpStatus.ACCEPTED);
        }
        throw new ResourceNotFoundException("Student not found in Trash");
    }

    @Override
    public ResponseEntity<?> deleteAllUserFromTrash() {
        List<UserTrashBin> usersTrash = userTrashBinRepository.findAll();
        if(usersTrash.size()!=0){
            userTrashBinRepository.deleteAll();
            return new ResponseEntity<>(new DeleteResponse("Users"),HttpStatus.ACCEPTED);
        }
        throw new ResourceNotFoundException("No User Found in Trash");
    }

    @Override
    public ResponseEntity<?> deleteAllTeacherFromTrash() {
        List<TeacherTrashBin> teachersTrash = teacherTrashBinRepository.findAll();
        if(teachersTrash.size()!=0){
            teacherTrashBinRepository.deleteAll();
            return new ResponseEntity<>(new DeleteResponse("Teachers"),HttpStatus.ACCEPTED);
        }
        throw  new ResourceNotFoundException("No Teacher Found in Trash");
    }

    @Override
    public ResponseEntity<?> deleteAllStudentFromTrash() {
        List<StudentTrashBin> studentsTrash = studentTrashBinRepository.findAll();
        if(studentsTrash.size()!=0){
            studentTrashBinRepository.deleteAll();
            return new ResponseEntity<>(new DeleteResponse("Students"),HttpStatus.ACCEPTED);
        }
        throw new ResourceNotFoundException("No Student Found in Trash");
    }


    @Override
    public ResponseEntity<?> getAllUsersInTrash(Integer pageNo,Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        return new ResponseEntity<>(userTrashBinRepository.findAll(pageable), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllTeachersFromTrash(Integer pageNo,Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        return new ResponseEntity<>(teacherTrashBinRepository.findAll(pageable), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllStudentFromTrash(Integer pageNo,Integer pageSize,String name) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        return new ResponseEntity<>(studentTrashBinRepository.findAll(pageable),HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> moveSubjectToTrash(String branchCode, Year from, Boards board, String standardLevel, String subjectCode) {
        Standard standard = findStandardHelper.findStandard(standardLevel,board,from,branchCode);
        Optional<Subject> existsSubject = standard.getSubjects().stream().filter(s->s.getSubjectCode().equals(subjectCode)).findAny();
        if(existsSubject.isPresent())
        {
            SubjectTrashBin subjectTrashBin = new SubjectTrashBin();
            subjectTrashBin.setBranchCode(branchCode);
            subjectTrashBin.setYearFrom(from);
            subjectTrashBin.setBoard(board);
            subjectTrashBin.setStandard(standardLevel);
            subjectTrashBin.setSubjects(existsSubject.get());
            subjectTrashBinRepository.save(subjectTrashBin);
            standard.getSubjects().remove(existsSubject.get());
            standardRepository.save(standard);
            return new ResponseEntity<>(new MoveToTrashResponse("Subject"),HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Subject Not Found");
    }

    @Override
    public ResponseEntity<?> undoSubjectFromTrash(String subjectCode) {
        SubjectTrashBin subjectTrashBin = subjectTrashBinRepository.findBySubjectsSubjectCode(subjectCode);
        if(subjectTrashBin!=null)
        {
            Standard standard = findStandardHelper.findStandard(subjectTrashBin.getStandard(),subjectTrashBin.getBoard(),subjectTrashBin.getYearFrom(),subjectTrashBin.getBranchCode());
            if(standard!=null)
            {
                Optional<Subject> subject = standard.getSubjects().stream().filter(s->s.getSubjectCode().equals(subjectCode)).findAny();
                if(!subject.isPresent())
                {
                    standard.getSubjects().add(subject.get());
                    standardRepository.save(standard);
                    subjectTrashBinRepository.delete(subjectTrashBin);
                    return new ResponseEntity<>(new UndoResponse("Subject"),HttpStatus.OK);
                }
                throw new AlreadyExistsException("Subject Already in Standard");
            }
            throw new ResourceNotFoundException("Standard not found");
        }
        throw new ResourceNotFoundException("Subject Not Found in trash");
    }

    @Override
    public ResponseEntity<?> getAllSubjectsInTrash(Integer pageNo,Integer pageSize,String sortBy) {
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
         return new ResponseEntity<>(subjectTrashBinRepository.findAll(pageable),HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> deleteSubjectPermanentlyFromTrash(String subjecCode) {
        SubjectTrashBin subjectTrashBin = subjectTrashBinRepository.findBySubjectsSubjectCode(subjecCode);
        if(subjectTrashBin!=null)
        {
            Subject subject = subjectTrashBin.getSubjects();
            subjectTrashBinRepository.delete(subjectTrashBin);
            subjectRepository.delete(subject);
            return new ResponseEntity<>(new DeleteResponse("Subject"),HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Subject Not Found In Trash");
    }

    @Override
    public ResponseEntity<?> deleteAllSubjectsPermanentlyFromTrash() {
        List<SubjectTrashBin> subjectTrashBins = subjectTrashBinRepository.findAll();
        if(!subjectTrashBins.isEmpty())
        {
            List<Subject> subjects = new ArrayList<>();
            for(SubjectTrashBin s: subjectTrashBins)
            {
                subjects.add(s.getSubjects());
            }
            subjectTrashBinRepository.deleteAll();
            subjectRepository.deleteAll(subjects);
        return new ResponseEntity<>(new DeleteResponse("All Subjects"),HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Trash is empty");
    }


}
