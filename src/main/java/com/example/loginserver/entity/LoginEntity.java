/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.loginserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author user
 */
@Entity
@Table(name = "login")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@NamedQueries({
        @NamedQuery(name = "LoginEntity.findAll", query = "SELECT l FROM LoginEntity l"),
        @NamedQuery(name = "LoginEntity.findById", query = "SELECT l FROM LoginEntity l WHERE l.id = :id"),
        @NamedQuery(name = "LoginEntity.findByUserId", query = "SELECT l FROM LoginEntity l WHERE l.userId = :userId"),
        @NamedQuery(name = "LoginEntity.findByPass", query = "SELECT l FROM LoginEntity l WHERE l.pass = :pass"),
        @NamedQuery(name = "LoginEntity.findByIp", query = "SELECT l FROM LoginEntity l WHERE l.ip = :ip"),
        @NamedQuery(name = "LoginEntity.findBySec", query = "SELECT l FROM LoginEntity l WHERE l.sec = :sec"),
        @NamedQuery(name = "LoginEntity.findBySecPass", query = "SELECT l FROM LoginEntity l WHERE l.secPass = :secPass")})
public class LoginEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "pass")
    private String pass;
    @Basic(optional = false)
    @Column(name = "ip")
    private String ip;
    @Basic(optional = false)
    @Column(name = "sec")
    private boolean sec;
    @Column(name = "sec_pass")
    private String secPass;


}
