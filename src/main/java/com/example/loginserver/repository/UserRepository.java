package com.example.loginserver.repository;
import com.example.loginserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    @Query(value = "SELECT e FROM UserEntity e where e.userName=:username")
    Optional<UserEntity> getByUserName(@Param("username") String userName);
}
