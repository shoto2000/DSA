package com.app.Repositories;

import com.app.Models.SubTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubTopicRepository extends JpaRepository<SubTopic,Integer> {
    @Query("SELECT S FROM SubTopic S WHERE S.subTopicName LIKE '%'||:subTopicName||'%'")
    public List<SubTopic> findBySubTopicNameKey(String subTopicName);
}
