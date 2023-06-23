package com.app.Services.UserService;

import com.app.Exceptions.BadRequestException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Exceptions.UserException;
import com.app.Models.*;
import com.app.Payloads.AddUserRequest;
import com.app.Payloads.ApiResponse;
import com.app.Payloads.MoveToTrashResponse;
import com.app.Payloads.ResetPasswordResponse;
import com.app.Repositories.*;
import com.app.Services.SchoolService.SchoolService;
import com.app.Services.TrashBinService.TrashBinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpVerificationRepository otpVerificationRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ScheduledExecutorService executorService;

    @Autowired
    private TrashBinService trashBinService;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserTrashBinRepository userTrashBinRepository;

    public void sendWelcomeMail(String email, String password, String name, Roles role) {
        logger.info("sendRestMail method invoked for sending mail");
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setSubject("Welcome To AIMEE International School");
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom("masailearn3@gmail.com");
            helper.setTo(email);
            String msg = "<div style=\"width:100%;font-family: Verdana, sans-serif;\">\n" + "  <div style=\"text-align:center;width:100%;background-color:#c7f9cc; padding:5px;\">\n" + "    <h1>AIMEE International School</h1>\n" + "  </div>\n" + "   <div style=\"width:80%;background-color:#fefcfb; padding:5px;margin:auto;\">\n" + "     <h2>Hello " + name + " !</h2>\n" + "     <p> Welcome to AIMEE International School as a " + role + " hope you will have a great journey ahead with us.</p> \n" + "     <p>Please find your credentials below to login into dashboard. </p>\n" + "     <h4>Email: " + email + "</h4>\n" + "     <h4>Password:" + password + " </h4>\n" + "     <div style=\"width:fit-content; margin:auto;\">\n" + "       <button style=\"background-color:#0496ff;border:none;border-radius:10px;padding:10px;\"><a style=\"color:#fff; text-decoration:none;\" href=\"\">Login To Dashboard</a></button>\n" + "     </div>\n" + "     <p> Please reset your password after login for first time.</p>\n" + "     <p> If you need some guidance about dashboard please find user guide here <a style=\"text-decoration:bold;color:blue;\" href=\"https://aimee.gitbook.io/aimee-management-portal/group-2/user-guides\">User Guides</a> .</p>\n" + "     <h4>Regards,<br> Administrator Team </h4>\n" + "  </div>\n" + "   <div style=\"text-align:center;width:100%;background-color:#c7f9cc; padding:5px\">\n" + "    <p>© 2023 AIMEE. All rights reserved</p>\n" + "  </div>\n" + "  \n" + "</div>";
            helper.setText(msg, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public User saveUser(AddUserRequest addUserRequest, String branchCode) {
        logger.info("save User invoked");
        Optional<User> findUser = userRepository.findByEmail(addUserRequest.getEmail());
        logger.info("Finding user with email: {}", addUserRequest.getEmail());
        if (!findUser.isPresent()) {
            logger.info("Setting details in user object");
            User newUser = new User();
            newUser.setName(addUserRequest.getName());
            newUser.setEmail(addUserRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));
            newUser.setRole(addUserRequest.getRole());
            newUser.setGender(addUserRequest.getGender());
            newUser.setBranchCode(branchCode);
            logger.info("Checking the roles");
            if (newUser.getRole() == Roles.Director) {
                logger.info("Role is director so calling assignDirector() for saving director");
                schoolService.assignDirector(branchCode, newUser);
            } else if (newUser.getRole() == Roles.Principal) {
                logger.info("Role is Principal so calling assignPrincipal()");
                schoolService.assignPrincipal(branchCode, newUser);
            } else if (newUser.getRole() == Roles.AdminStaff) {
                schoolService.addAdminStaff(branchCode, newUser);
            } else if (newUser.getRole() == Roles.AcademicAuditor) {
                schoolService.addAcademicAuditor(branchCode, newUser);
            }
            else {
                logger.info("So role director or principal not found so saving with role: {}", addUserRequest.getRole());
                userRepository.save(newUser);
            }
            logger.info("Sending welcome email with login credentials");
            sendWelcomeMail(addUserRequest.getEmail(), addUserRequest.getPassword(), addUserRequest.getName(), addUserRequest.getRole());
            logger.info("Returning newuser object");
            return newUser;
        }
        logger.warn("User already exists with email: {}", addUserRequest.getEmail());
        throw new UserException("User already found with email ");

    }

    @Override
    public ResponseEntity<?> deleteUserByEmail(String email) {
        Optional<User> checkUser = userRepository.findByEmail(email);
        Optional<UserTrashBin> checkUserTrashBin = userTrashBinRepository.findByEmail(email);

        if (checkUserTrashBin.isPresent() && checkUser.isPresent()) {
            userTrashBinRepository.delete(checkUserTrashBin.get());
        }
        if (checkUser.isPresent()) {
            School getSchool = new School();
            User getUser = checkUser.get();
            if (getUser.getRole().equals(Roles.Principal)) {
                getSchool = schoolRepository.findAll()
                        .stream()
                        .filter(s -> s.getPrincipleOfSchool() != null && s.getPrincipleOfSchool().getEmail().equals(email))
                        .findFirst()
                        .orElse(null);
                schoolRepository.findAll()
                        .stream()
                        .filter(s -> s.getPrincipleOfSchool() != null && s.getPrincipleOfSchool().getEmail().equals(email))
                        .findFirst()
                        .ifPresent(s -> s.setPrincipleOfSchool(null));
            } else if (getUser.getRole().equals(Roles.Director)) {
                getSchool = schoolRepository.findAll()
                        .stream()
                        .filter(s -> s.getDirectorOfSchool() != null && s.getDirectorOfSchool().getEmail().equals(email))
                        .findFirst()
                        .orElse(null);
                schoolRepository.findAll()
                        .stream()
                        .filter(s -> s.getDirectorOfSchool() != null && s.getDirectorOfSchool().getEmail().equals(email))
                        .findFirst()
                        .ifPresent(s -> s.setDirectorOfSchool(null));
            } else if (getUser.getRole().equals(Roles.AcademicAuditor)) {
                getSchool = schoolRepository.findAll()
                        .stream()
                        .filter(s -> s.getAcademicAuditors().stream().anyMatch(a -> a.getEmail().equals(email)))
                        .findFirst()
                        .orElse(null);
                schoolRepository.findAll()
                        .stream()
                        .forEach(s -> s.getAcademicAuditors().removeIf(a -> a.getEmail().equals(email)));
            } else if (getUser.getRole().equals(Roles.AdminStaff)) {
                logger.info("Admin Staff deletion invoked");
                getSchool = schoolRepository.findAll()
                        .stream()
                        .filter(s -> s.getAdminStaff().stream().anyMatch(a -> a.getEmail().equals(email)))
                        .findFirst()
                        .orElse(null);
                schoolRepository.findAll()
                        .stream()
                        .forEach(s -> s.getAdminStaff().removeIf(a -> a.getEmail().equals(email)));
                System.out.println(getSchool.getBranchCode());
            }
            else if(getUser.getRole().equals(Roles.Teacher)){
                logger.info("Admin Staff deletion invoked");
                getSchool = schoolRepository.findAll()
                        .stream()
                        .filter(s -> s.getTeachers().stream().anyMatch(a -> a.getTeacherEmail().equals(email)))
                        .findFirst()
                        .orElse(null);
                schoolRepository.findAll()
                        .stream()
                        .forEach(s -> s.getAdminStaff().removeIf(a -> a.getEmail().equals(email)));
                System.out.println(getSchool.getBranchCode());
            }
            trashBinService.moveUserToTrashBin(getUser);
            userRepository.delete(getUser);
            return new ResponseEntity<>(new MoveToTrashResponse("User"), HttpStatus.OK);
        } else {
            throw new UserException("User not found");
        }
    }

    @Override
    public ResponseEntity<List<?>> getListOfUsers() {
        logger.info("Finding all users in user table");
        List<User> users = userRepository.findAll();
        logger.info("Returning list of users");
        return new ResponseEntity<>(users, HttpStatus.FOUND);

    }

    @Override
    public ResponseEntity<?> uploadImage(String email, MultipartFile imageFile) throws IOException {
        logger.info("uploadImage() invoked for uploading profile image and finding user with email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            logger.info("User found so uploading image");
            User existingUser = user.get();
            existingUser.setImage(imageFile.getBytes());
            logger.info("Image uploaded");
            return new ResponseEntity<>(userRepository.save(existingUser), HttpStatus.ACCEPTED);
        }
        logger.warn("User not found");
        throw new ResourceNotFoundException("User not found");

    }

    @Override
    public ResponseEntity<?> getImage(String email) {
        logger.info("getImage() invoked for getting profile image and finding user with email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            logger.info("User found and returning image");
            User existingUser = user.get();
            return ResponseEntity.accepted().contentType(MediaType.IMAGE_JPEG).body(existingUser.getImage());
        }
        logger.warn("User not found");
        throw new ResourceNotFoundException("User not found");
    }

    public void sendOtpMail(String email, String name, Integer otp) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setSubject("One Time Password Verification");
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom("masailearn3@gmail.com");
            helper.setTo(email);
            String msg = "<div style=\"width:100%;font-family: Verdana, sans-serif;\">\n" + "  <div style=\"text-align:center;width:100%;background-color:#caf0f8; padding:5px;\">\n" + "    <h1>AIMEE International School</h1>\n" + "  </div>\n" + "   <div style=\"width:80%;background-color:#fefcfb; padding:5px;margin:auto;\">\n" + "     <h2>Hello " + name + " !</h2>\n" + "     <p>We got a Password Reset request from your account if that was not you please ignore this email and don't share otp to anyone.</p> \n" + "     <p>Please find otp for verification below. </p>\n" + "     <div style=\"width:fit-content; margin:auto;\">\n" + "       <h1>" + otp + "</h1>\n" + "     </div>\n" + "     <p> If you need some guidance about dashboard please find user guide here <a style=\"text-decoration:bold;color:blue\" href=\"https://aimee.gitbook.io/aimee-management-portal/group-2/user-guides\">User Guides</a> .</p>\n" + "     <h4>Regards,<br> Administrator Team </h4>\n" + "  </div>\n" + "   <div style=\"text-align:center;width:100%;background-color:#caf0f8; padding:5px\">\n" + "    <p>© 2023 AIMEE. All rights reserved</p>\n" + "  </div>\n" + "  \n" + "</div>";
            helper.setText(msg, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> requestOtp(String email) {
        logger.info("requestOtp method invoked for sending otp on mail");
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            Random rand = new Random();
            Integer otp = rand.nextInt(899999) + 100000;
            sendOtpMail(email, user.get().getName(), otp);
            OtpVerification otpVerification = new OtpVerification();
            otpVerification.setOtp(otp);
            otpVerification.setEmail(email);
            otpVerificationRepository.save(otpVerification);
            executorService.schedule(() -> otpVerificationRepository.delete(otpVerification), 2, TimeUnit.MINUTES);
            return new ResponseEntity<>(new ApiResponse(true, "Otp send successfully"), HttpStatus.OK);
        }
        throw new UserException("User not found");
    }

    @Override
    public ResponseEntity<?> forgetPassword(String email, Integer otp, String password) {
        OtpVerification otpVerification = otpVerificationRepository.findByEmail(email);
        if (otpVerification != null) {
            if (otpVerification.getOtp().equals(otp)) {
                Optional<User> existsUser = userRepository.findByEmail(email);
                if (existsUser.isPresent()) {
                    User user = existsUser.get();
                    if (user.getRole() == Roles.Teacher) {
                        user.setPassword(passwordEncoder.encode(password));
                        Teacher teacher = teacherRepository.findByTeacherEmail(email);
                        teacher.setPassword(passwordEncoder.encode(password));
                        userRepository.save(user);
                        teacherRepository.save(teacher);
                        return new ResponseEntity<>(new ResetPasswordResponse(Roles.Teacher), HttpStatus.ACCEPTED);
                    } else {
                        user.setPassword(passwordEncoder.encode(password));
                        userRepository.save(user);
                        return new ResponseEntity<>(new ResetPasswordResponse(user.getRole()), HttpStatus.ACCEPTED);
                    }
                }
            }
            throw new BadCredentialsException("Invalid Otp");
        }
        throw new ResourceNotFoundException("Otp expired or not sent");
    }


    public ResponseEntity<?> changePassword(String email, String oldPassword, String newPassword) {
        Optional<User> loginUser = userRepository.findByEmail(email);
        if (loginUser.isPresent()) {

            User user = loginUser.get();

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, oldPassword));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            if (user.getRole() == Roles.Teacher) {
                user.setPassword(passwordEncoder.encode(newPassword));
                Teacher teacher = teacherRepository.findByTeacherEmail(email);
                teacher.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                teacherRepository.save(teacher);
                return new ResponseEntity<>(new ResetPasswordResponse(Roles.Teacher), HttpStatus.ACCEPTED);
            } else {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return new ResponseEntity<>(new ResetPasswordResponse(user.getRole()), HttpStatus.ACCEPTED);
            }
        }
        throw new UserException("User not found");
    }

    @Override
    public ResponseEntity<?> updateNameOfUser(String email, String name) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent())
        {
            if(user.get().getRole().equals(Roles.Teacher))
            {
                Teacher teacher = teacherRepository.findByTeacherEmail(email);
                if(teacher!=null)
                {
                    teacher.setTeacherName(name);
                    teacherRepository.save(teacher);
                    user.get().setName(name);
                    userRepository.save(user.get());
                    return new ResponseEntity<>(new ApiResponse(true,"Name successfully updated"),HttpStatus.ACCEPTED);
                }
                throw new ResourceNotFoundException("Teacher Not Found");
            }
            else{
                user.get().setName(name);
                userRepository.save(user.get());
                return new ResponseEntity<>(new ApiResponse(true,"Name successfully updated"),HttpStatus.ACCEPTED);
            }
        }
        throw new ResourceNotFoundException("User Not Found");
    }

    @Override
    public ResponseEntity<?> updateEmailOfUser(String email, String newEmail) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent())
        {
            if(user.get().getRole().equals(Roles.Teacher))
            {
                Teacher teacher = teacherRepository.findByTeacherEmail(email);
                if(teacher!=null)
                {
                    teacher.setTeacherEmail(newEmail);
                    teacherRepository.save(teacher);
                    user.get().setEmail(newEmail);
                    userRepository.save(user.get());
                    return new ResponseEntity<>(new ApiResponse(true,"Email successfully updated"),HttpStatus.ACCEPTED);
                }
                throw new ResourceNotFoundException("Teacher Not Found");
            }
            else{
                user.get().setEmail(newEmail);
                userRepository.save(user.get());
                return new ResponseEntity<>(new ApiResponse(true,"Email successfully updated"),HttpStatus.ACCEPTED);
            }
        }
        throw new ResourceNotFoundException("User Not Found");
    }
}
