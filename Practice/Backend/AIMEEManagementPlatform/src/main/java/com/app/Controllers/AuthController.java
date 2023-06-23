package com.app.Controllers;


import com.app.Exceptions.UserException;
import com.app.Models.Roles;
import com.app.Models.School;
import com.app.Models.Teacher;
import com.app.Models.User;
import com.app.Payloads.ApiResponse;
import com.app.Payloads.AuthResponse;
import com.app.Payloads.SignInRequest;
import com.app.Repositories.SchoolRepository;
import com.app.Repositories.TeacherRepository;
import com.app.Repositories.UserRepository;
import com.app.Security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TokenProvider tokenProvider;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest loginRequest) {

        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isPresent()) {
            User existingUser = user.get();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenProvider.createToken(authentication);

            if (existingUser.getRole().equals(Roles.Director)) {
                School school = schoolRepository.findByDirectorOfSchool(existingUser);
                return ResponseEntity.ok(new AuthResponse(token, existingUser.getRole(), school.getBranchCode()));
            }
            else if(existingUser.getRole().equals(Roles.Principal))
            {
                School school = schoolRepository.findByPrincipleOfSchool(existingUser);
                return ResponseEntity.ok(new AuthResponse(token,existingUser.getRole(),school.getBranchCode()));
            }
            else if(existingUser.getRole().equals(Roles.AcademicAuditor))
            {
                School school = schoolRepository.findByAcademicAuditors(existingUser);
                return ResponseEntity.ok(new AuthResponse(token,existingUser.getRole(),school.getBranchCode()));
            }
            else if(existingUser.getRole().equals(Roles.AdminStaff))
            {
                School school = schoolRepository.findByAdminStaff(existingUser);
                return ResponseEntity.ok(new AuthResponse(token,existingUser.getRole(),school.getBranchCode()));
            }
            else if(existingUser.getRole().equals(Roles.Teacher))
            {
                Teacher teacher = teacherRepository.findByTeacherEmail(existingUser.getEmail());
                return ResponseEntity.ok(new AuthResponse(token,existingUser.getRole(),teacher.getBranchCode()));
            }
            else{
                return ResponseEntity.ok(new AuthResponse(token,existingUser.getRole()));
            }

        }
        throw new UserException("User not found");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            return new ResponseEntity<>(new ApiResponse(true, "User Successfully logout"), HttpStatus.OK);
        }

        throw new UserException("User is not authenticated");
    }


}
