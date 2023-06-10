package com.savitech.fintab.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "tbl_sec_manager")
public class SecManager {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "passcode", nullable = false, columnDefinition = "bytea")
    private byte[] passcode;

    @Column(name = "locator", nullable = false)
    private String locator;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
