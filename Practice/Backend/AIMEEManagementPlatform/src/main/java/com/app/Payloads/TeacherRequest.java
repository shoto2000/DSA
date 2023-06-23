package com.app.Payloads;

import com.app.Models.Boards;
import com.app.Models.Genders;
import com.app.Models.SubjectStandardToTeacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRequest {
    private String teacherName;

    private String teacherEmail;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genders gender;

    private String password;

    private String branchCode;

    private String classTeacherOfStandard;

    private Set<SubjectStandardToTeacher> assignSubjectInAStandard;
}
