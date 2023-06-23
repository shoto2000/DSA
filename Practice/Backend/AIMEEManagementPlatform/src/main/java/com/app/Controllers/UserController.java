package com.app.Controllers;

import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.User;
import com.app.Payloads.AddUserRequest;
import com.app.Payloads.ApiResponse;
import com.app.Repositories.UserRepository;
import com.app.Security.CurrentUser;
import com.app.Security.UserPrincipal;
import com.app.Services.TrashBinService.TrashBinService;
import com.app.Services.UserService.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.net.URI;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TrashBinService trashBinService;

    @GetMapping("/me")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director')")
    @PostMapping("/add")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AddUserRequest addUserRequest,@RequestParam("branch-code") String branchCode) {

        User newUser = userService.saveUser(addUserRequest,branchCode);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, newUser.getRole()+" registered successfully"));
    }

    @PreAuthorize("hasAnyRole('Administrator')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserByEmail(@Email @RequestParam String email){
        return userService.deleteUserByEmail(email);
    }

    @GetMapping("get/all")
    public ResponseEntity<?> getAllUsers()
    {
        return userService.getListOfUsers();
    }

    @PreAuthorize("hasAnyRole('Administrator','Principal','Director','Teacher','AdminStaff','AcademicAuditor')")
    @PutMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("email") String email, @RequestParam("image") MultipartFile imageFile) throws IOException {
        return userService.uploadImage(email, imageFile);
    }

    @GetMapping("/image")
    public ResponseEntity<?> getImage(@RequestParam("email") String email)
    {
        return userService.getImage(email);
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestParam("email") String email)
    {
        return userService.requestOtp(email);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgetPassword(@RequestParam("email") String email,@RequestParam("otp") Integer otp,@RequestParam("password") String password)
    {
        return userService.forgetPassword(email, otp, password);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(String email,@RequestParam("old-password") String oldPassword,@RequestParam("new-password") String newPassword)
    {
        return userService.changePassword(email,oldPassword,newPassword);
    }


    @PreAuthorize("hasAnyRole('Administrator')")
    @DeleteMapping("/trash/undo")
    public ResponseEntity<?> undoUserFromTrash(@RequestParam("email") String email){
        return trashBinService.undoUserFromTrash(email);
    }

    @PreAuthorize("hasAnyRole('Administrator')")
    @DeleteMapping("/trash/delete")
    public ResponseEntity<?> DeleteUserFromTrash(@RequestParam("email") String email){
        return trashBinService.deleteUserFromTrash(email);
    }

    @PreAuthorize("hasAnyRole('Administrator')")
    @GetMapping("/trash/get/all")
    public ResponseEntity<?> getAllUserInTrash(@RequestParam("page-no")Integer pageNo,
                                               @RequestParam("page-size")Integer pageSize,
                                               @RequestParam("sort-by")String sortBy){
        return trashBinService.getAllUsersInTrash(pageNo,pageSize,sortBy);
    }

    @DeleteMapping("/trash/delete/all")
    public ResponseEntity<?> emptyTrashBinForUsers(){
        return trashBinService.deleteAllUserFromTrash();
    }

    @PutMapping("/edit/name")
    public ResponseEntity<?> updateNameOfUser(@RequestParam("email") String email,@RequestParam("name") String name )
    {
        return userService.updateNameOfUser(email, name);
    }

    @PutMapping("/edit/email")
    public ResponseEntity<?> updateEmailOfUser(@RequestParam("current-email") String email,@RequestParam("new-email") String name )
    {
        return userService.updateEmailOfUser(email, name);
    }

}
