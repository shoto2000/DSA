package com.app.Services.StandardService;

import com.app.Exceptions.AlreadyExistsException;
import com.app.Exceptions.ResourceNotFoundException;
import com.app.Helpers.FindBoardHelper;
import com.app.Helpers.PaginationHelper;
import com.app.Models.Boards;
import com.app.Models.SchoolBoard;
import com.app.Models.Standard;
import com.app.Payloads.MoveToTrashResponse;
import com.app.Repositories.BoardRepository;
import com.app.Services.TrashBinService.TrashBinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StandardServiceImpl implements StandardService {

    @Autowired
    private FindBoardHelper findBoardHelper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TrashBinService trashBinService;

    private static final Logger logger = LoggerFactory.getLogger(StandardServiceImpl.class);

    @Override
    public ResponseEntity<?> addStandard(Standard standard, Boards board, Year yearFrom, String branchCode) {
        logger.info("addStandard method invoked for adding standard");
        logger.info("Finding board using findBoard Helper");
        SchoolBoard foundBoard = findBoardHelper.findBoard(board, yearFrom, branchCode);
        logger.info("Finding standard in board");
        Optional<Standard> existsStandard = foundBoard.getStandards().stream().filter(b -> b.getStandardLevel().equals(standard.getStandardLevel())).findAny();
        if (!existsStandard.isPresent()) {
            logger.info("Standard Not Found So Adding new standard");
            foundBoard.getStandards().add(standard);
            logger.info("Saving and returning the standard in board ");
            return new ResponseEntity<>(boardRepository.save(foundBoard), HttpStatus.CREATED);
        }
        logger.warn("Standard Already Exists");
        throw new AlreadyExistsException("Standard already exists in this board");
    }

    @Override
    public ResponseEntity<?> getStandard(String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("getStandard method invoked for get Standard in a board");
        logger.info("Finding board using findBoardHelper");
        SchoolBoard foundBoard = findBoardHelper.findBoard(board, yearFrom, branchCode);
        logger.info("Finding standard in board");
        Optional<Standard> standard = foundBoard.getStandards().stream().filter(s -> s.getStandardLevel().equals(standardLevel)).findAny();
        if (standard.isPresent()) {
            logger.info("Standard Found and returning");
            return new ResponseEntity<>(standard.get(), HttpStatus.FOUND);
        }
        logger.warn("Standard not Found");
        throw new ResourceNotFoundException("Standard not found");

    }

    @Override
    public ResponseEntity<?> getAllStandardsInBoard(Boards board, Year yearFrom, String branchCode, Integer pageNo, Integer pageSize, String sortBy) {
        logger.info("GetAllStandardInBoard method invoked and finding board using findBoardHelper");
        SchoolBoard foundBoard = findBoardHelper.findBoard(board, yearFrom, branchCode);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy));
        List<Standard> standards = foundBoard.getStandards();
        Page<Standard> pageStandards = PaginationHelper.getPage(standards,pageable);
        return new ResponseEntity<>(pageStandards, HttpStatus.FOUND);
    }


    @Override
    public ResponseEntity<?> moveStandardToTrash(String standardLevel, Boards board, Year yearFrom, String branchCode) {
        logger.info("deleteStandard() invoked");
        SchoolBoard foundBoard = findBoardHelper.findBoard(board, yearFrom, branchCode);
        logger.info("Finding standard");
        Optional<Standard> existsStandard = foundBoard.getStandards().stream().filter(s -> s.getStandardLevel().equals(standardLevel)).findAny();
        if (existsStandard.isPresent()) {
            logger.info("Standard found and moving to trash");
            trashBinService.moveStandardToTrashBin(branchCode, yearFrom, board, existsStandard.get());
            foundBoard.getStandards().remove(existsStandard.get());
            boardRepository.save(foundBoard);
            logger.info("Saved standard in board");
            return new ResponseEntity<>(new MoveToTrashResponse("Standard"), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Standard not found");

    }
}
