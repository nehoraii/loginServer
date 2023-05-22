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
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@NamedQueries({
        @NamedQuery(name = "UserEntity.findAll", query = "SELECT u FROM UserEntity u"),
        @NamedQuery(name = "UserEntity.findById", query = "SELECT u FROM UserEntity u WHERE u.id = :id"),
        @NamedQuery(name = "UserEntity.findByName", query = "SELECT u FROM UserEntity u WHERE u.name = :name"),
        @NamedQuery(name = "UserEntity.findBySecName", query = "SELECT u FROM UserEntity u WHERE u.secName = :secName"),
        @NamedQuery(name = "UserEntity.findByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email"),
        @NamedQuery(name = "UserEntity.findByPhone", query = "SELECT u FROM UserEntity u WHERE u.phone = :phone"),
        @NamedQuery(name = "UserEntity.findByUserName", query = "SELECT u FROM UserEntity u WHERE u.userName = :userName"),
        @NamedQuery(name = "UserEntity.findByBirthDay", query = "SELECT u FROM UserEntity u WHERE u.birthDay = :birthDay"),
        @NamedQuery(name = "UserEntity.findBySecretKey", query = "SELECT u FROM UserEntity u WHERE u.secretKey = :secretKey")})
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "sec_name")
    private String secName;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "phone")
    private String phone;
    @Basic(optional = false)
    @Column(name = "user_name")
    private String userName;
    @Basic(optional = false)
    @Column(name = "birth_day")
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay;
    @Basic(optional = false)
    @Column(name = "secret_key")
    private String secretKey;

}
