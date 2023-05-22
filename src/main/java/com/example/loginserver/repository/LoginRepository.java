package com.example.loginserver.repository;

import com.example.loginserver.entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LoginRepository extends JpaRepository<LoginEntity,Long> {
}
