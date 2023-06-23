package com.app.Repositories;

import com.app.Models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject,Integer> {
    public Subject findBySubjectCode(String subjectCode);
}
