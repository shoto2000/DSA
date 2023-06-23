package com.app.Repositories;

import com.app.Models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    public Teacher findByTeacherEmail(String email);

}
