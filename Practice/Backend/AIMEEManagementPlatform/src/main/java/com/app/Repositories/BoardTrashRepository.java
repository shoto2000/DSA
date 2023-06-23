package com.app.Repositories;

import com.app.Models.BoardTrashBin;
import com.app.Models.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardTrashRepository extends JpaRepository<BoardTrashBin,Integer> {

    public BoardTrashBin findBySchoolBoardBoard(Boards board);

}
