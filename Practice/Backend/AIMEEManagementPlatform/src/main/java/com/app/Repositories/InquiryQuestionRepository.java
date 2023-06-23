package com.app.Repositories;

import com.app.Models.InquiryQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryQuestionRepository extends JpaRepository<InquiryQuestions,Integer> {

}
