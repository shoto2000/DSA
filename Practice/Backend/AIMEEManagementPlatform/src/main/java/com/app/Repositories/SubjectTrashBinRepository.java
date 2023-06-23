package com.app.Repositories;

import com.app.Models.SubjectTrashBin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectTrashBinRepository extends JpaRepository<SubjectTrashBin,Integer> {

    public SubjectTrashBin findBySubjectsSubjectCode(String subjectCode);

}
