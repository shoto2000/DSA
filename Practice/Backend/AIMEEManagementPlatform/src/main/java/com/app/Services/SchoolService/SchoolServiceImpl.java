package com.app.Services.SchoolService;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Exceptions.UserException;
import com.app.Helpers.FindAcademicYearHelper;
import com.app.Models.*;
import com.app.Payloads.FileResponse;
import com.app.Payloads.MoveToTrashResponse;
import com.app.Repositories.AcademicYearRepository;
import com.app.Repositories.SchoolRepository;
import com.app.Services.TrashBinService.TrashBinService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService {


    private static final Logger logger = LoggerFactory.getLogger(SchoolServiceImpl.class);

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Autowired
    private TrashBinService trashBinService;

    @Autowired
    private FindAcademicYearHelper academicYearHelper;


    @Override
    public ResponseEntity<?> addSchool(School addSchool) {
        logger.info("addSchool method invoked");
        logger.info("Fetching the School Branch");
        School findSchoolBranch = schoolRepository.findByBranchCode(addSchool.getBranchCode());
        logger.info("Checking if School Branch is present or not");
        if (findSchoolBranch == null && addSchool.getBranchCode().length()!=0 && addSchool.getBranchAddress().length()!=0) {
            logger.info("Saving the School Branch and Returning School Response Entity");
            return new ResponseEntity<>(schoolRepository.save(addSchool), HttpStatus.CREATED);
        }
        else if(addSchool.getBranchCode().length()==0){
            throw new AlreadyExistsException("Branch Code can not be null");
        }
        else if(addSchool.getBranchAddress().length()==0){
            throw new AlreadyExistsException("School Address can not be null");
        }
        logger.info("Warning School Branch Already Exist");
        throw new AlreadyExistsException("School Branch already found with branchCode ");

    }

    @Override
    public ResponseEntity<?> addAcademicYearToSchool(AcademicYear academicYear, String branchCode) {
        logger.info("addAcademicYearToSchool method invoked");
        logger.info("Fetching the School Branch");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School Branch is present or not");
        if (school != null) {
            logger.info("Searching of Academic year");
            Optional<AcademicYear> existsAcademicYear = school.getAcademicYears().stream().filter(a -> a.getAcademicYearFrom().equals(academicYear.getAcademicYearFrom())).findAny();
            logger.info("Checking if Academic year is present or not");
            if (!existsAcademicYear.isPresent()) {
                logger.info("Adding the Academic year");
                school.getAcademicYears().add(academicYear);
                logger.info("Saving the School and Returning School Response Entity");
                return new ResponseEntity<>(schoolRepository.save(school), HttpStatus.CREATED);
            }
            logger.info("Warning Academic year Already Exist");
            throw new AlreadyExistsException("Academic Year Already Exists");
        }
        logger.info("Warning School Branch not Found");
        throw new ResourceNotFoundException("School Branch not found");
    }

    @Override
    public ResponseEntity<?> getAcademicYear(Year yearFrom, String branchCode) {
        logger.info("getAcademicYear method invoked");
        logger.info("Fetching the School Branch");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School Branch is present or not");
        if (school != null) {
            logger.info("Searching of Academic year");
            Optional<AcademicYear> academicYear = school.getAcademicYears().stream().filter(a -> a.getAcademicYearFrom().equals(yearFrom)).findAny();
            logger.info("Checking if Academic year is present or not");
            if (academicYear.isPresent()) {
                logger.info("Returning Response Academic year");
                return new ResponseEntity<>(academicYear.get(), HttpStatus.FOUND);
            }
            logger.info("Warning Academic year not Found");
            throw new ResourceNotFoundException("Academic Year Not Found");
        }
        logger.info("Warning School Branch Not Found");
        throw new ResourceNotFoundException("School Branch Not Found");
    }

    @Override
    public ResponseEntity<?> getAllAcademicYears(String branchCode) {
        logger.info("getAllAcademicYears method invoked");
        logger.info("Fetching the School Branch");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School Branch is present or not");
        if (school != null) {
            logger.info("Returning Response of all Academic years");
            return new ResponseEntity<>(school.getAcademicYears(), HttpStatus.FOUND);
        }
        logger.info("Warning School Branch Not Found");
        throw new ResourceNotFoundException("School Branch Not Found");
    }

    @Override
    public ResponseEntity<?> uploadDataToAcademicYear(String branchCode, Year yearFrom, MultipartFile file) {
        AcademicYear academicYearExists = academicYearHelper.findAcademicYear(yearFrom, branchCode);
        if (academicYearExists != null) {
            if (academicYearExists.getBoards().isEmpty()) {

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    logger.info("Registering the module for checking year");
                    objectMapper.registerModule(new JavaTimeModule());
                    AcademicYear academicYear = objectMapper.readValue(file.getBytes(), AcademicYear.class);
                    if (academicYear.getAcademicYearFrom().equals(academicYearExists.getAcademicYearFrom())) {
                        academicYearExists.setBoards(academicYear.getBoards());
                        academicYearRepository.save(academicYearExists);
                        return new ResponseEntity<>(new FileResponse(true, "Upload", "Academic Year Data"), HttpStatus.ACCEPTED);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            throw new AlreadyExistsException("Academic Year Data Already Exists");
        }
        throw new ResourceNotFoundException("Academic Year not found in this branch");
    }


    @Override
    public ResponseEntity<?> downloadAcademicYearFromDatabase(String branchCode, Year yearFrom) {
        AcademicYear academicYear = academicYearHelper.findAcademicYear(yearFrom, branchCode);
        try {
            if (academicYear != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                byte[] fileContent = new byte[0];
                fileContent = objectMapper.writeValueAsBytes(academicYear);
                return ResponseEntity.ok()
                        .contentLength(fileContent.length)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"academicYear.json\"")
                        .body(fileContent);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        throw new ResourceNotFoundException("Academic Year Not Found");

    }

    @Override
    public ResponseEntity<?> moveYearToTrash(Year yearFrom, String branchCode) {
        logger.info("moveYearToTrash() invoked for moving academic year to trash");
        logger.info("Findign school");
        School existsSchool = schoolRepository.findByBranchCode(branchCode);
        if (existsSchool != null) {
            logger.info("School found so now finding academic year in school");
            Optional<AcademicYear> academicYear = existsSchool.getAcademicYears().stream().filter(a -> a.getAcademicYearFrom().equals(yearFrom)).findAny();
            if (academicYear.isPresent()) {
                logger.info("Academic year found so now moving academic year to academic year trash");
                trashBinService.moveAcademicYearToTrashBin(branchCode, academicYear.get());
                logger.info("Removing academic year from school");
                existsSchool.getAcademicYears().remove(academicYear.get());
                schoolRepository.save(existsSchool);
                logger.info("Academic year moved to trash and now returning response");
                return new ResponseEntity<>(new MoveToTrashResponse("Academic Year"), HttpStatus.OK);
            }
            throw new ResourceNotFoundException("Academic Year Not Found");
        }
        throw new ResourceNotFoundException("School Not Found");
    }


    @Override
    public ResponseEntity<?> updateBranchAddress(School school) {
        logger.info("updateBranchAddress method invoked");
        logger.info("Fetching the School Branch");
        School existsSchool = schoolRepository.findByBranchCode(school.getBranchCode());
        logger.info("Checking if School Branch is present or not");
        if (existsSchool != null) {
            logger.info("Edit Branch Address of existing School");
            existsSchool.setBranchAddress(school.getBranchAddress());
            logger.info("Saving the School and Returning School Response");
            return new ResponseEntity<>(schoolRepository.save(existsSchool), HttpStatus.OK);
        }
        logger.info("Warning School Branch Not Found");
        throw new ResourceNotFoundException("School Branch not found");
    }


    @Override
    public ResponseEntity<?> deleteSchool(String branchCode) {
        School school = schoolRepository.findByBranchCode(branchCode);
        if (school != null) {
            schoolRepository.delete(school);
            return new ResponseEntity<>(new MoveToTrashResponse("School"), HttpStatus.OK);
        }
        logger.info("Warning School Not Found");
        throw new ResourceNotFoundException("School not found");

    }

    @Override
    public ResponseEntity<?> getSchool(String branchCode) {
        logger.info("getSchool method invoked");
        logger.info("Fetching the School Branch");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School Branch is present or not");
        if (school != null) {
            logger.info("Returning School Response");
            return new ResponseEntity<>(school, HttpStatus.FOUND);
        }
        logger.info("Warning School Branch Not Found");
        throw new ResourceNotFoundException("School Branch Not Found");
    }

    @Override
    public ResponseEntity<?> getAllBranches(Integer pageNo, Integer pageSize, String name) {
        logger.info("getAllBranches method invoked");
        logger.info("Fetching all the School Branches");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(name));
        logger.info("Returning School Branch Responses");
        return new ResponseEntity<>(schoolRepository.findAll(pageable), HttpStatus.FOUND);
    }

    @Override
    public void assignDirector(String branchCode, User user) {
        logger.info("assignDirector method invoked");
        logger.info("Fetching the School Branch");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School is present or not");
        if (school != null) {
            logger.info("Checking if Director is present or not");
            if (school.getDirectorOfSchool() == null) {
                logger.info("Adding the Director to the School Branch");
                school.setDirectorOfSchool(user);
                logger.info("Saving the School Branch");
                schoolRepository.save(school);
                logger.info("Returning");
                return;
            }
            logger.info("Warning Director Already Exist");
            throw new AlreadyExistsException("Director Already Present");
        }
        logger.info("Warning School Branch Not Found");
        throw new ResourceNotFoundException("School Branch Not Found");
    }

    @Override
    public void assignPrincipal(String branchCode, User user) {
        logger.info("assignPrincipal method invoked");
        logger.info("Fetching the School Branch");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School Branch is present or not");
        if (school != null) {
            logger.info("Checking if Principal is present or not");
            if (school.getPrincipleOfSchool() == null) {
                logger.info("Adding the Principal to the School Branch");
                school.setPrincipleOfSchool(user);
                logger.info("Saving the School Branch");
                schoolRepository.save(school);
                logger.info("Returning");
                return;
            }
            logger.info("Warning Principal Already Exist");
            throw new AlreadyExistsException("Principle Already Present");
        }
        logger.info("Warning School Branch Not Found");
        throw new ResourceNotFoundException("School Branch Not Found");
    }

    @Override
    public void addAdminStaff(String branchCode, User user) {
        logger.info("add AdminStaff method invoked");
        logger.info("Fetching the School Branch");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School Branch is present or not");
        if (school != null) {
            logger.info("Added admin staff to the school branch");
            school.getAdminStaff().add(user);
            logger.info("Saving the School Branch");
            schoolRepository.save(school);
            logger.info("Returning");
            return;
        }
        logger.info("Warning School Branch Not Found");
        throw new ResourceNotFoundException("School Branch Not Found");
    }

    @Override
    public void addAcademicAuditor(String branchCode, User user) {
        logger.info("add Academic Auditor method invoked");
        logger.info("Fetching the School Branch");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School Branch is present or not");
        if (school != null) {
            logger.info("Added Academic Auditor to the school branch");
            school.getAcademicAuditors().add(user);
            logger.info("Saving the School Branch");
            schoolRepository.save(school);
            logger.info("Returning");
            return;
        }
        logger.info("Warning School Branch Not Found");
        throw new ResourceNotFoundException("School Branch Not Found");
    }


    @Override
    public ResponseEntity<?> addBoard(Boards board, Year academicYearfrom, String branchCode) {
        logger.info("addBoard method invoked");
        logger.info("Fetching the School Branch");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School Branch is present or not");
        if (school != null) {
            logger.info("Searching of Academic year");
            Optional<AcademicYear> existsAcademicYear = school.getAcademicYears().stream().filter(a -> a.getAcademicYearFrom().equals(academicYearfrom)).findAny();
            logger.info("Checking if Academic year is present or not");
            if (existsAcademicYear.isPresent()) {
                logger.info("Searching of School Board");
                Optional<SchoolBoard> existsSchoolBoard = existsAcademicYear.get().getBoards().stream().filter(b -> b.getBoard().equals(board)).findAny();
                logger.info("Checking if School Board is present or not");
                if (!existsSchoolBoard.isPresent()) {
                    logger.info("Creating new Object of School Board");
                    SchoolBoard schoolBoard = new SchoolBoard();
                    logger.info("Adding the new Board to the School Board");
                    schoolBoard.setBoard(board);
                    logger.info("Adding the School Board to the Academic year");
                    existsAcademicYear.get().getBoards().add(schoolBoard);
                    logger.info("saving and returning the Response of Academic year");
                    return new ResponseEntity<>(academicYearRepository.save(existsAcademicYear.get()), HttpStatus.CREATED);
                }
                logger.info("Warning School Board Already Exist");
                throw new AlreadyExistsException("School Board Already Exists In This Year");
            }
            logger.info("Warning Academic year not Found");
            throw new ResourceNotFoundException("Academic Year Not Found");
        }
        logger.info("Warning School Branch not Found");
        throw new ResourceNotFoundException("School Branch not found");
    }

    @Override
    public ResponseEntity<?> getBoard(Boards board, Year academicYearfrom, String branchCode) {
        logger.info("getBoard method invoked");
        logger.info("Fetching the School Branch");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School Branch is present or not");
        if (school != null) {
            logger.info("Searching of Academic year");
            Optional<AcademicYear> existsAcademicYear = school.getAcademicYears().stream().filter(a -> a.getAcademicYearFrom().equals(academicYearfrom)).findAny();
            logger.info("Checking if Academic year is present or not");
            if (existsAcademicYear.isPresent()) {
                logger.info("Searching of School Board");
                Optional<SchoolBoard> existsSchoolBoard = existsAcademicYear.get().getBoards().stream().filter(b -> b.getBoard().equals(board)).findAny();
                logger.info("Checking if School Board is present or not");
                if (existsSchoolBoard.isPresent()) {
                    logger.info("Returning the Response of Academic year");
                    return new ResponseEntity<>(existsSchoolBoard.get(), HttpStatus.FOUND);
                }
                logger.info("Warning School Board not Found");
                throw new ResourceNotFoundException("School Board Not Found In This Year");
            }
            logger.info("Warning Academic year not Found");
            throw new ResourceNotFoundException("Academic Year Not Found");
        }
        logger.info("Warning School Branch not Found");
        throw new ResourceNotFoundException("School Branch not found");
    }

    @Override
    public ResponseEntity<?> getAllBoards(String branchCode, Year academicYearfrom) {
        logger.info("getAllBoards method invoked");
        logger.info("Fetching the School Branch");
        School school = schoolRepository.findByBranchCode(branchCode);
        logger.info("Checking if School Branch is present or not");
        if (school != null) {
            logger.info("Searching of Academic year");
            Optional<AcademicYear> existsAcademicYear = school.getAcademicYears().stream().filter(a -> a.getAcademicYearFrom().equals(academicYearfrom)).findAny();
            logger.info("Checking if Academic year is present or not");
            if (existsAcademicYear.isPresent()) {
                logger.info("Returning Response of all the School Board present in the Academic year");
                return new ResponseEntity<>(existsAcademicYear.get().getBoards(), HttpStatus.FOUND);
            }
            logger.info("Warning Academic year not Found");
            throw new ResourceNotFoundException("Academic Year Not Found");
        }
        logger.info("Warning School Branch not Found");
        throw new ResourceNotFoundException("School Branch not found");
    }

}
