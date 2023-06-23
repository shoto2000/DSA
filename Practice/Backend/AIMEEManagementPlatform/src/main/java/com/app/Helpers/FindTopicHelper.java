package com.app.Helpers;

import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.Boards;
import com.app.Models.Topic;
import com.app.Models.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Optional;

@Component
public class FindTopicHelper {

    private static final Logger logger = LoggerFactory.getLogger(FindTopicHelper.class);

    @Autowired
    private FindUnitHelper findUnitOOps;

    public Topic findTopic(String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode){
        logger.info("FindTopic method invoked");
        logger.info("Fetching the Unit");
        Unit foundUnit = findUnitOOps.findUnit(unitTitle,subjectName,standardLevel,board,yearFrom,branchCode);
        logger.info("Searching for Topic");
        Optional<Topic> existsTopic = foundUnit.getTopics().stream().filter(t -> t.getTopicName().equals(topicName)).findAny();
        logger.info("Checking if Topic present or not");
        if(existsTopic.isPresent()){
            logger.info("Returning Topic");
            return existsTopic.get();
        }
        logger.info("Warning Topic not Found");
        throw  new ResourceNotFoundException("Topic not found");
    }

}
