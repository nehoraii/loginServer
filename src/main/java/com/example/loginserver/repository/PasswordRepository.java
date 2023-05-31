package com.example.loginserver.repository;
import com.example.loginserver.entity.PasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordEntity,Long> {
    @Query(value = "SELECT * FROM password WHERE user_id=?1",nativeQuery = true)
    Optional<List<PasswordEntity>>getAllById(long userId);
}
