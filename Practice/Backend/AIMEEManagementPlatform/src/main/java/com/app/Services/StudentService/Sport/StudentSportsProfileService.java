package com.app.Services.StudentService.Sport;

import org.springframework.http.ResponseEntity;

public interface StudentSportsProfileService {

    public ResponseEntity<?> addColumnToProfile(String columnName,String dataType);

    public ResponseEntity<?> addValueToProfile(String studentCode,String key, String value);

    public ResponseEntity<?> getSportsProfileOfStudent(String studentCode);

    public ResponseEntity<?> getAllColumnsAndDatatypeofSportsProfile();

    public ResponseEntity<?> updateColumnNameOfSportsProfile(String columnName,String newColumnName);

}
