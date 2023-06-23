package com.app.Repositories;

import com.app.Models.Student;
import com.app.Models.StudentTrashBin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentTrashBinRepository extends JpaRepository<StudentTrashBin,Long> {

    public Optional<StudentTrashBin> findByStudentCode(String codOrEmail);

}
