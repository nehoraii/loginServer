/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.loginserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


/**
 *
 * @author user
 */
@Entity
@Table(name = "password",schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Data
//קלאס המייצג את טבלת הססמאות ב-DB
public class PasswordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "user_id")
    private Long userId;
    @Basic(optional = false)
    @Column(name = "pass")
    private String pass;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


}
