package com.app.Services.Dashboard;

import org.springframework.http.ResponseEntity;

public interface DashboardService {

    public ResponseEntity<?> getDashboardData();

    public ResponseEntity<?> getAllDirectors(Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> getAllPrincipals(Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> getAllAdminStaffs(Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> getAllAcademicAuditor(Integer pageNo,Integer pageSize,String name);

    public ResponseEntity<?> getAllTeachers(Integer pageNo,Integer pageSize,String name);


}
