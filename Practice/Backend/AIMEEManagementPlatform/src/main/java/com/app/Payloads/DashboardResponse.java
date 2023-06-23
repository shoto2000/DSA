package com.app.Payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private Integer totalBranches;

    private Integer totalDirectors;

    private Integer totalPrincipals;

    private Integer academicAuditor;

    private Integer totalAdminStaff;

    private Integer totalTeachers;

    private Integer totalStudents;

}
