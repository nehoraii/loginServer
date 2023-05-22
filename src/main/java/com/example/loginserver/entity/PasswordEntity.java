/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.loginserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author user
 */
@Entity
@Table(name = "password")
@AllArgsConstructor
@NoArgsConstructor
@Data
@NamedQueries({
        @NamedQuery(name = "PasswordEntity.findAll", query = "SELECT p FROM PasswordEntity p"),
        @NamedQuery(name = "PasswordEntity.findById", query = "SELECT p FROM PasswordEntity p WHERE p.id = :id"),
        @NamedQuery(name = "PasswordEntity.findByUserId", query = "SELECT p FROM PasswordEntity p WHERE p.userId = :userId"),
        @NamedQuery(name = "PasswordEntity.findByPass", query = "SELECT p FROM PasswordEntity p WHERE p.pass = :pass"),
        @NamedQuery(name = "PasswordEntity.findByDate", query = "SELECT p FROM PasswordEntity p WHERE p.date = :date")})

public class PasswordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "user_id")
    private long userId;
    @Basic(optional = false)
    @Column(name = "pass")
    private String pass;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


}
