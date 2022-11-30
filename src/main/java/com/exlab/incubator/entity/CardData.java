package com.exlab.incubator.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "card_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 16, name = "card_number")
    private String cardNumber;

    @Column(nullable = false, length = 30, name = "card_owner")
    private String cardOwner;

    @Column(nullable = false, length = 5, name = "expiration_date")
    private String expirationDate;

    @Column(nullable = false, length = 4, name = "cvv_code")
    private int CVVCode;

    @Column(name = "total_balance")
    private Double totalBalance;
}
