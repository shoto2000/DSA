package com.app.Services.StudentService.CoCurriculum;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.StudentCoCurriculumActivityProfile;
import com.app.Payloads.ApiResponse;
import com.app.Payloads.ColumnsResponse;
import com.app.Repositories.StudentCoCurriculumActivityProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentCoCurriculumActivityProfileServiceImpl implements StudentCoCurriculumActivityProfileService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StudentCoCurriculumActivityProfileRepository studentCoCurriculumActivityProfileRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentCoCurriculumActivityProfileServiceImpl.class);

    @Override
    @Transactional
    public ResponseEntity<?> addColumnToProfile(String columnName, String dataType) {
        String newKey = columnName.replaceAll("(?<=[a-z])(?=[A-Z])|\\s+", "_").toLowerCase();
        logger.info("Checking column is already present");
        if(studentCoCurriculumActivityProfileRepository.columnExistsOrNoT(newKey)==0)
        {
            logger.info("Adding a new Column");
            String sql = "ALTER TABLE student_co_curriculum_activity_profile ADD COLUMN " + newKey +" "+dataType;
            logger.info("Creating Query");
            Query query = entityManager.createNativeQuery(sql);
            logger.info("Updating the Query");
            query.executeUpdate();
            logger.info("Returning the Response");
            return new ResponseEntity<>(new ApiResponse(true,"Column Added successfully"), HttpStatus.CREATED);
        }
        throw new AlreadyExistsException("Column Already Exists");
    }

    @Override
    @Transactional
    public ResponseEntity<?> addValueToProfile(String studentCode, String columnName, String value) {
        StudentCoCurriculumActivityProfile studentCoCurriculumActivityProfile = studentCoCurriculumActivityProfileRepository.findByStudentCode(studentCode);
        if (studentCoCurriculumActivityProfile != null) {
            String newKey = columnName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
            if (studentCoCurriculumActivityProfileRepository.columnExistsOrNoT(newKey) > 0) {
                String sql = "UPDATE student_co_curriculum_activity_profile SET " + newKey + " = '" + value + "' WHERE student_code = '" + studentCode + "'";
                Query query = entityManager.createNativeQuery(sql);
                query.executeUpdate();
                return new ResponseEntity<>(new ApiResponse(true,"Value added successfully"), HttpStatus.OK);
            }
            throw new ResourceNotFoundException("Column Not Found");
        }
        throw new ResourceNotFoundException("Student Not Found");
    }

    @Override
    public ResponseEntity<?> getProfileOfStudent(String studentCode) {
        List<String> columns = studentCoCurriculumActivityProfileRepository.findColumns();
        String profile = studentCoCurriculumActivityProfileRepository.findProfile(studentCode);
        String[] s = profile.split(",");
        Map<String,String> coCurriculumActivityProfile = new LinkedHashMap<>();
        for(int i=0;i< columns.size();i++)
        {
            coCurriculumActivityProfile.put(columns.get(i),s[i]);
            System.out.println(columns.get(i)+" "+s[i]);
        }
        return new ResponseEntity<>(coCurriculumActivityProfile, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllColumns() {
        List<String[]> metaData = studentCoCurriculumActivityProfileRepository.getTableMetadata();
        List<ColumnsResponse> columns = metaData.stream().map((m) -> {
            String updatedString = m[0].replaceAll("_", " ");
            String[] stringArr = updatedString.split(" ");

            String finalString = "";
            for(int i=0;i<stringArr.length;i++)
            {
                finalString += stringArr[i].substring(0, 1).toUpperCase() + stringArr[i].substring(1);
                finalString += " ";
            }
            finalString = finalString.trim();

            ColumnsResponse columnResponse = new ColumnsResponse();
            columnResponse.setColumnName(finalString);
            if(m[1].equals("varchar")) columnResponse.setDataType("Text");
            else if(m[1].equals("int")) columnResponse.setDataType("Number");
            else if(m[1].equals("bool")) columnResponse.setDataType("Boolean");
            else if(m[1].equals("date")) columnResponse.setDataType("Date");
            else if(m[1].equals("time")) columnResponse.setDataType("Time");
            else if(m[1].equals("year")) columnResponse.setDataType("Year");
            else columnResponse.setDataType(m[1]);
            return columnResponse;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(columns,HttpStatus.FOUND);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateCoCurriculumProfileOfStudent(String columnName, String newColumnName) {
        String oldColumn = columnName.replaceAll("(?<=[a-z])(?=[A-Z])|\\s+", "_").toLowerCase();
        String newColumn = newColumnName.replaceAll("(?<=[a-z])(?=[A-Z])|\\s+", "_").toLowerCase();
        if(studentCoCurriculumActivityProfileRepository.columnExistsOrNoT(oldColumn)==1)
        {
            String sql = "ALTER TABLE student_co_curriculum_activity_profile  RENAME COLUMN "+ oldColumn +" TO "+newColumn;
            Query query = entityManager.createNativeQuery(sql);
            query.executeUpdate();
            return new ResponseEntity<>(new ApiResponse(true,"Column Updated successfully"), HttpStatus.CREATED);
        }
        throw new AlreadyExistsException(columnName+" Not Found");
    }
}
