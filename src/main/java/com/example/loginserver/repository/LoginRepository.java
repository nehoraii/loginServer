package com.example.loginserver.repository;

import com.example.loginserver.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LoginRepository extends JpaRepository<LoginEntity,Long> {
    @Query(value = "SELECT * FROM login where user_id=?1 ORDER BY DESC LIMIT ?2",nativeQuery = true)
    Optional<List<LoginEntity>> getLastThree(long id,int sizeBLock);
    @Query(value = "SELECT * FROM login where user_id=?1 AND 'date'+?3>=?2 ",nativeQuery = true)
    Optional<List<LoginEntity>> getSpam(long id, Date date,int timeBetweenSpam);

}
