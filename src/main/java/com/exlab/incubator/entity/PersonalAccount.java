package com.exlab.incubator.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "personal_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String accountNumber;

    @Column(name = "created_at")
    private Instant createdAt;
}
