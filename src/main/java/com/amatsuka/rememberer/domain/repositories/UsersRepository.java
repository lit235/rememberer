package com.amatsuka.rememberer.domain.repositories;

import com.amatsuka.rememberer.domain.entities.QUser;
import com.amatsuka.rememberer.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends QueryDslRepository<User, QUser, Long> {
    Optional<User> findByUsername(String username);
}
