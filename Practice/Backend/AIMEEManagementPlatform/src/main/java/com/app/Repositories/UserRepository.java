package com.app.Repositories;

import com.app.Models.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public void deleteByEmail(String email);

    public List<User> findAllByRole(Roles role);
    public Page<User> findAllByRole(Roles role, Pageable pageable);

}
