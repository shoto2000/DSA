package com.app.Services.StudentService.Sport;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.StudentSportsProfile;
import com.app.Payloads.ApiResponse;
import com.app.Payloads.ColumnsResponse;
import com.app.Repositories.StudentRepository;
import com.app.Repositories.StudentSportsProfileRepository;
import com.app.Services.StudentService.Sport.StudentSportsProfileService;
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
public class StudentSportsProfileServiceImpl implements StudentSportsProfileService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StudentSportsProfileRepository studentSportsProfileRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional
    public ResponseEntity<?> addColumnToProfile(String columnName,String dataType) {
        String newKey = columnName.replaceAll("(?<=[a-z])(?=[A-Z])|\\s+", "_").toLowerCase();
        if(studentSportsProfileRepository.columnExistsOrNoT(newKey)==0)
        {
            String sql = "ALTER TABLE student_sports_profile ADD COLUMN " + newKey +" "+dataType;
            Query query = entityManager.createNativeQuery(sql);
            query.executeUpdate();
            return new ResponseEntity<>(new ApiResponse(true,"Column Added successfully"), HttpStatus.CREATED);
        }
        throw new AlreadyExistsException("Column Already Exists");
    }

    @Override
    @Transactional
    public ResponseEntity<?> addValueToProfile(String studentCode, String columnName, String value) {
        StudentSportsProfile studentSportsProfile = studentSportsProfileRepository.findByStudentCode(studentCode);
        if (studentSportsProfile != null) {
            String newKey = columnName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
            if (studentSportsProfileRepository.columnExistsOrNoT(newKey) > 0) {
                String sql = "UPDATE student_sports_profile SET " + newKey + " = '" + value + "' WHERE student_code = '" + studentCode + "'";
                Query query = entityManager.createNativeQuery(sql);
                query.executeUpdate();
                return new ResponseEntity<>(new ApiResponse(true,"Value added successfully"), HttpStatus.OK);
            }
            throw new ResourceNotFoundException("Column Not Found");
        }
        throw new ResourceNotFoundException("Student Not Found");
    }

    @Override
    public ResponseEntity<?> getSportsProfileOfStudent(String studentCode) {
        List<String> columns = studentSportsProfileRepository.findColumns();
        String profile = studentSportsProfileRepository.findProfile(studentCode);
        String[] s = profile.split(",");
        Map<String,String> sportsProfile = new LinkedHashMap<>();
        for(int i=0;i< columns.size();i++)
        {
            sportsProfile.put(columns.get(i),s[i]);
            System.out.println(columns.get(i)+" "+s[i]);
        }
        return new ResponseEntity<>(sportsProfile, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllColumnsAndDatatypeofSportsProfile() {
        List<String[]> metaData = studentSportsProfileRepository.getTableMetadata();
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
    public ResponseEntity<?> updateColumnNameOfSportsProfile(String columnName,String newColumnName) {
        String oldColumn = columnName.replaceAll("(?<=[a-z])(?=[A-Z])|\\s+", "_").toLowerCase();
        String newColumn = newColumnName.replaceAll("(?<=[a-z])(?=[A-Z])|\\s+", "_").toLowerCase();
        if(studentSportsProfileRepository.columnExistsOrNoT(oldColumn)==1)
        {
            String sql = "ALTER TABLE student_sports_profile  RENAME COLUMN "+ oldColumn +" TO "+newColumn;
            Query query = entityManager.createNativeQuery(sql);
            query.executeUpdate();
            return new ResponseEntity<>(new ApiResponse(true,"Column Updated successfully"), HttpStatus.CREATED);
        }
        throw new AlreadyExistsException(columnName+" Not Found");
    }

}
