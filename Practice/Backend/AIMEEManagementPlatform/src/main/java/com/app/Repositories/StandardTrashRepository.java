package com.app.Repositories;

import com.app.Models.StandardTrashBin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardTrashRepository extends JpaRepository<StandardTrashBin,Integer> {
    public StandardTrashBin findByStandardStandardLevel(String standardLevel);
}
