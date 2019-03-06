package com.amatsuka.rememberer.domain.repositories;

import com.amatsuka.rememberer.domain.entities.QUser;
import com.amatsuka.rememberer.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends QueryDslRepository<User, QUser, Long> {
}
