package com.app.Services.Dashboard;

import com.app.Models.Roles;
import com.app.Models.User;
import com.app.Payloads.DashboardResponse;
import com.app.Repositories.SchoolRepository;
import com.app.Repositories.StudentRepository;
import com.app.Repositories.TeacherRepository;
import com.app.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> getDashboardData() {
        DashboardResponse dashboardResponse = new DashboardResponse();
        dashboardResponse.setTotalBranches(schoolRepository.findAll().size());
        dashboardResponse.setTotalDirectors(userRepository.findAllByRole(Roles.Director).size());
        dashboardResponse.setTotalPrincipals(userRepository.findAllByRole(Roles.Principal).size());
        dashboardResponse.setTotalAdminStaff(userRepository.findAllByRole(Roles.AdminStaff).size());
        dashboardResponse.setAcademicAuditor(userRepository.findAllByRole(Roles.AcademicAuditor).size());
        dashboardResponse.setTotalTeachers(userRepository.findAllByRole(Roles.Teacher).size());
        dashboardResponse.setTotalStudents(studentRepository.findAll().size());
        return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllDirectors(Integer pageNo, Integer pageSize, String name) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by(name));
        return new ResponseEntity<>(userRepository.findAllByRole(Roles.Director,pageable), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllPrincipals(Integer pageNo, Integer pageSize, String name) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by(name));
        return new ResponseEntity<>(userRepository.findAllByRole(Roles.Principal,pageable), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllAdminStaffs(Integer pageNo, Integer pageSize, String name) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by(name));
        return new ResponseEntity<>(userRepository.findAllByRole(Roles.AdminStaff,pageable), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllAcademicAuditor(Integer pageNo, Integer pageSize, String name) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by(name));
        return new ResponseEntity<>(userRepository.findAllByRole(Roles.AcademicAuditor,pageable), HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> getAllTeachers(Integer pageNo, Integer pageSize, String name) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by(name));
        return new ResponseEntity<>(userRepository.findAllByRole(Roles.Teacher,pageable), HttpStatus.FOUND);
    }


}
