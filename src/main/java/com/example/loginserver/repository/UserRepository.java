package com.example.loginserver.repository;
import com.example.loginserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    @Query("SELECT e FROM UserEntity e where e.userName=?1")
    List<UserEntity> getByUserName(String userName);
}
