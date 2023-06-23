package com.app.Repositories;

import com.app.Models.Boards;
import com.app.Models.SchoolBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<SchoolBoard,Integer> {
    public SchoolBoard findByBoard(Boards board);
}