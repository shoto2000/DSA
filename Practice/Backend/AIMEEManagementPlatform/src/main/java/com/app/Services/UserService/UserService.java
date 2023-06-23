package com.app.Services.UserService;


import com.app.Models.User;
import com.app.Payloads.AddUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    public User saveUser(AddUserRequest addUserRequest,String branchCode);

    public ResponseEntity<?> deleteUserByEmail(String email);

    public ResponseEntity<List<?>> getListOfUsers();

    public ResponseEntity<?> uploadImage(String email, MultipartFile imageFile) throws IOException;

    public ResponseEntity<?> getImage(String email);

    public ResponseEntity<?> requestOtp(String email);

    public ResponseEntity<?> forgetPassword(String email,Integer otp,String password);
    
	public ResponseEntity<?> changePassword(String email,String oldPassword, String newPassword);

    public ResponseEntity<?> updateNameOfUser(String email,String name);

    public ResponseEntity<?> updateEmailOfUser(String email,String newEmail);


}
