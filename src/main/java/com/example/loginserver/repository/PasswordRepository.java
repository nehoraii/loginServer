package com.example.loginserver.repository;
import com.example.loginserver.entity.PasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//ממשק האחראי לחיבור  למסד הנתונים ומביא ממנו את המידע הרלוונטי לפי השאילתה
public interface PasswordRepository extends JpaRepository<PasswordEntity,Long> {
    /*
    מקבלת: המזהה הייחודי של המשתמש.
    מבצעת: מביאה את כל הסיסמאות של המשתמש.
    מחזירה: רשימה של הסיסמאות.

    */
    @Query(value = "SELECT * FROM password WHERE user_id=?1",nativeQuery = true)
    Optional<List<PasswordEntity>>getAllById(Long userId);
}
