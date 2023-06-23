package com.app.Repositories;

import com.app.Models.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification,Integer> {
    public OtpVerification findByEmail(String email);
}
