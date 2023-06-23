package com.app.Services.StudentService.Behaviour;

import org.springframework.http.ResponseEntity;

public interface StudentBehaviouralProfileService {
    public ResponseEntity<?> addColumnToProfile(String columnName, String dataType);

    public ResponseEntity<?> addValueToProfile(String studentCode,String key, String value);

    public ResponseEntity<?> getBehaviouralProfileOfStudent(String studentCode);

    public ResponseEntity<?> getAllColumnsAndDatatype();

    public ResponseEntity<?> updateBehaviourColumnProfile(String columnName,String newColumnName);

}
