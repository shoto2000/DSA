package com.app.Services.StudentService.General;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.StudentGeneralProfile;
import com.app.Payloads.ApiResponse;
import com.app.Payloads.ColumnsResponse;
import com.app.Repositories.StudentGeneralProfileRepository;
import com.app.Repositories.StudentRepository;
import com.app.Services.StudentService.General.StudentGeneralProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentGeneralProfileServiceImpl implements StudentGeneralProfileService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StudentGeneralProfileRepository generalProfileRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional
    public ResponseEntity<?> addColumnToProfile(String columnName , String dataType ) {
        String newKey = columnName.replaceAll("(?<=[a-z])(?=[A-Z])|\\s+", "_").toLowerCase();
        if(generalProfileRepository.columnExistsOrNoT(newKey)==0)
        {
            String sql = "ALTER TABLE student_general_profile ADD COLUMN " + newKey +" "+dataType;
            Query query = entityManager.createNativeQuery(sql);
            query.executeUpdate();
            return new ResponseEntity<>(new ApiResponse(true,"Column Added successfully"), HttpStatus.CREATED);
        }
        throw new AlreadyExistsException("Column Already Exists");
    }

    @Override
    @Transactional
    public ResponseEntity<?> addValueToProfile(String studentCode, String columnName, String value) {
        StudentGeneralProfile studentGeneralProfile = generalProfileRepository.findByStudentCode(studentCode);
        if (studentGeneralProfile != null) {
                String newKey = columnName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
            if (generalProfileRepository.columnExistsOrNoT(newKey) > 0) {
                String sql = "UPDATE student_general_profile SET " + newKey + " = '" + value + "' WHERE student_code = '" + studentCode + "'";
                Query query = entityManager.createNativeQuery(sql);
                query.executeUpdate();
                return new ResponseEntity<>(new ApiResponse(true,"Value added successfully"), HttpStatus.OK);
            }
            throw new ResourceNotFoundException("Column Not Found");
        }
        throw new ResourceNotFoundException("Student Not Found");
    }

    @Override
    public ResponseEntity<?> getGeneralProfileOfStudent(String studentCode) {
        List<String> columns = generalProfileRepository.findColumns();
        String profile = generalProfileRepository.findProfile(studentCode);
        String[] s = profile.split(",");
        Map<String,String> generalProfile = new LinkedHashMap<>();
        for(int i=0;i< columns.size();i++)
        {
            generalProfile.put(columns.get(i),s[i]);
            System.out.println(columns.get(i)+" "+s[i]);
        }
        return new ResponseEntity<>(generalProfile, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getColumnsAndDataTypeOfTable() {
        List<String[]> metaData = generalProfileRepository.getTableMetadata();
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
    public ResponseEntity<?> updateColumnName(String columnName, String newColumnName) {
        String oldColumn = columnName.replaceAll("(?<=[a-z])(?=[A-Z])|\\s+", "_").toLowerCase();
        String newColumn = newColumnName.replaceAll("(?<=[a-z])(?=[A-Z])|\\s+", "_").toLowerCase();
        if(generalProfileRepository.columnExistsOrNoT(oldColumn)==1)
        {
            String sql = "ALTER TABLE student_general_profile RENAME COLUMN "+ oldColumn +" TO "+newColumn;
            Query query = entityManager.createNativeQuery(sql);
            query.executeUpdate();
            return new ResponseEntity<>(new ApiResponse(true,"Column Updated successfully"), HttpStatus.CREATED);
        }
        throw new AlreadyExistsException(columnName+" Not Found");
    }

}
