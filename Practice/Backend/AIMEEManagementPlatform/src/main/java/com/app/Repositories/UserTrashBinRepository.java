package com.app.Repositories;

import com.app.Models.UserTrashBin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTrashBinRepository extends JpaRepository<UserTrashBin,Long> {
    public Optional<UserTrashBin> findByEmail(String email);
}
