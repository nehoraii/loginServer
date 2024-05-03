package com.example.loginserver.repository;
import com.example.loginserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//ממשק האחראי לחיבור  למסד הנתונים ומביא ממנו את המידע הרלוונטי לפי השאילתה
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    /*
    מקבלת: "שם המשתמש" של המשתמש.
    מבצעת: מביאה את השורה שמקיימת שהעמודה שלה היא לפי המחרוזת שהפונקציה קיבלה.
מחזירה: אובייקט שמייצג את המשתמש, UserEntity.
    */
    @Query(value = "SELECT e FROM UserEntity e where e.userName=:username")
    Optional<UserEntity> getByUserName(@Param("username") String userName);
}
