package com.example.loginserver.repository;

import com.example.loginserver.entity.LoginEntity;
import com.example.loginserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface LoginRepository extends JpaRepository<LoginEntity,Long> {
    @Query(value = "SELECT * FROM login where user_id=?1 ORDER BY \"date\" DESC LIMIT ?2",nativeQuery = true)
    Optional<List<LoginEntity>> getLastThree(Long id,int sizeBLock);
    @Query(value = "SELECT * FROM login WHERE user_id = ?1 AND \"date\" >= ?2", nativeQuery = true)
    Optional<List<LoginEntity>> getSpam(Long id, Timestamp timestamp);
    @Query(value = "SELECT e FROM UserEntity e WHERE e.id=:id")
    Optional<UserEntity>getUserByUserId(@Param("id")Long id);
    //222222//
    /*@Query(value = "SELECT e FROM PasswordEntity e where e.userId=:id order by e.date DESC")
    List<PasswordEntity> getPassByUserId2(@Param("id") long userId);*/
    @Query(value = "SELECT * FROM password where user_id=:id order by \"date\" DESC LIMIT 1",nativeQuery = true)
    Object[] getPassByUserId(@Param("id") Long userId);
    //אם לא עובד להשתמש ב-2
   /* @Query(value = "SELECT * FROM login WHERE user_id=?1 AND sec=true ORDER BY id DESC LIMIT 1",nativeQuery = true)
    Optional<LoginEntity> getLoginObjectByUserId(Long userId);
    */
   /* @Query("SELECT e from LoginEntity e WHERE e.id=:id")
    Optional<LoginEntity> getSecretKey(@Param("id")Long userId);*/
}
