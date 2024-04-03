package com.oftwapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="certification")
@Table(name="certification")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CertificationEntity {
    @Id
    private String username;
    private String email;
    private String certificationNumber;
}
