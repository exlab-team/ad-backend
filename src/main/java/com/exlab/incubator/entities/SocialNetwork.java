package com.exlab.incubator.entities;

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
@Table(name = "social_networks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SocialNetwork {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 150)
    private String link;

    public SocialNetwork(String name) {
        this.name = name;
    }

    public SocialNetwork(String name, String link) {
        this.name = name;
        this.link = link;
    }
}
