package com.app.Repositories;


import com.app.Models.Standard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardRepository extends JpaRepository<Standard,Integer> {
    public Standard findByStandardLevel(String standard);


}
