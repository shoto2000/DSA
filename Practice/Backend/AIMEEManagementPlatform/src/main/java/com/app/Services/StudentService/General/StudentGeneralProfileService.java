package com.app.Services.StudentService.General;

import com.app.Models.Student;
import org.springframework.http.ResponseEntity;

public interface StudentGeneralProfileService {

    public ResponseEntity<?> addColumnToProfile(String columnName,String dataType);

    public ResponseEntity<?> addValueToProfile(String studentCode,String key, String value);

    public ResponseEntity<?> getGeneralProfileOfStudent(String studentCode);

    public ResponseEntity<?> getColumnsAndDataTypeOfTable();

    public ResponseEntity<?> updateColumnName(String columnName,String newColumnName);

}
