package com.app.Repositories;

import com.app.Models.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QueryRepository extends JpaRepository<Query,Integer> {

    public Optional<Query> findByQueryCode(String queryCode);
    public List<Query> findByQueryCodeOrQueryTitleContainingIgnoreCaseOrderByQueryTitle(String queryTitle, String queryCode);

}
