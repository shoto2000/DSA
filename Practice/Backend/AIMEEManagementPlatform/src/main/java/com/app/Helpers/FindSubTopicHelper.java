package com.app.Helpers;

import com.app.Exceptions.ResourceNotFoundException;
import com.app.Models.Boards;
import com.app.Models.SubTopic;
import com.app.Models.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.Optional;

@Component
public class FindSubTopicHelper {

    private static final Logger logger = LoggerFactory.getLogger(FindSubjectHelper.class);

    @Autowired
    private FindTopicHelper findTopicOOPs;

    public SubTopic findSubTopic(String subTopicName, String topicName, String unitTitle, String subjectName, String standardLevel, Boards board, Year yearFrom, String branchCode){
        logger.info("FindSubTopic method invoked");
        logger.info("Fetching the Topic");
        Topic foundTopic = findTopicOOPs.findTopic(topicName,unitTitle, subjectName, standardLevel, board,yearFrom, branchCode);
        logger.info("Searching for Subtopic");
        Optional<SubTopic> existsSubtopic = foundTopic.getSubTopics().stream().filter(s -> s.getSubTopicName().equals(subTopicName)).findAny();
        logger.info("Checking if Subtopic is present or not");
        if(existsSubtopic.isPresent()){
            logger.info("Returning the Subtopic");
            return existsSubtopic.get();
        }
        logger.info("Warning Subtopic not Found");
        throw new ResourceNotFoundException("Subtopic not found");
    }
}
