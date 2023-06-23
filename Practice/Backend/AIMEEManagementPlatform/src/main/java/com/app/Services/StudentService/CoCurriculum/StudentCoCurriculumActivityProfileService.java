package com.app.Services.StudentService.CoCurriculum;

import org.springframework.http.ResponseEntity;

public interface StudentCoCurriculumActivityProfileService {
    public ResponseEntity<?> addColumnToProfile(String columnName, String dataType);

    public ResponseEntity<?> addValueToProfile(String studentCode,String key, String value);

    public ResponseEntity<?> getProfileOfStudent(String studentCode);

    public ResponseEntity<?> getAllColumns();

    public ResponseEntity<?> updateCoCurriculumProfileOfStudent(String columnName,String newColumnName);

}
