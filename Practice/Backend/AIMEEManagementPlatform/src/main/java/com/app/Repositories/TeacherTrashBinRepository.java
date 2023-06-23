package com.app.Repositories;

import com.app.Models.TeacherTrashBin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherTrashBinRepository extends JpaRepository<TeacherTrashBin,Integer> {
    public Optional<TeacherTrashBin> findByTeacherEmail(String email);
}