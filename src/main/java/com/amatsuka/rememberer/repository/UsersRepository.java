package com.amatsuka.rememberer.repository;

import com.amatsuka.rememberer.domain.entity.QUser;
import com.amatsuka.rememberer.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends QueryDslRepository<User, QUser, Long> {
    Optional<User> findByUsername(String username);
}
