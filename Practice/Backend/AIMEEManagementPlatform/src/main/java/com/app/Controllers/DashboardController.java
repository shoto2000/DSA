package com.app.Controllers;

import com.app.Services.Dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/get")
    public ResponseEntity<?> dashboardData()
    {
       return dashboardService.getDashboardData();
    }

    @GetMapping("/get/all/directors")
    public ResponseEntity<?> getAllDirectors(@RequestParam("page-no") Integer pageNo, @RequestParam("page-size") Integer pageSize,@RequestParam("sort-by") String name)
    {
        return dashboardService.getAllDirectors(pageNo, pageSize, name);
    }

    @GetMapping("/get/all/principals")
    public ResponseEntity<?> getAllPrincipals(@RequestParam("page-no") Integer pageNo, @RequestParam("page-size") Integer pageSize,@RequestParam("sort-by") String name)
    {
        return dashboardService.getAllPrincipals(pageNo, pageSize, name);
    }

    @GetMapping("/get/all/admin-staffs")
    public ResponseEntity<?> getAllAdminStaffs(@RequestParam("page-no") Integer pageNo, @RequestParam("page-size") Integer pageSize,@RequestParam("sort-by") String name)
    {
        return dashboardService.getAllAdminStaffs(pageNo,pageSize,name);
    }

    @GetMapping("/get/all/academic-auditors")
    public ResponseEntity<?> getAllAcademicAuditor(@RequestParam("page-no") Integer pageNo, @RequestParam("page-size") Integer pageSize,@RequestParam("sort-by") String name)
    {
        return dashboardService.getAllAcademicAuditor(pageNo,pageSize,name);
    }

    @GetMapping("/get/all/teachers")
    public ResponseEntity<?> getAllTeachers(@RequestParam("page-no") Integer pageNo, @RequestParam("page-size") Integer pageSize,@RequestParam("sort-by") String name)
    {
        return dashboardService.getAllTeachers(pageNo, pageSize, name);
    }

}
