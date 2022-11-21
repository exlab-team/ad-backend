package com.exlab.incubator.entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 70, unique = true)
    private String email;

    @Column(length = 30, unique = true, name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "time_of_sending_the_confirmation_link")
    private Date timeOfSendingTheConfirmationLink;

    @Column(length = 50, name = "activation_code")
    private String activationCode;

    @OneToOne
    @JoinColumn(name = "personal_account_id", referencedColumnName = "id")
    private PersonalAccount personalAccount;

    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @ManyToMany
    @JoinTable(
        name = "users_unconnected_social_networks",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "social_network_id")
    )
    private List<SocialNetwork> unconnectedSocialNetworks;

    @ManyToMany
    @JoinTable(
        name = "users_connected_social_networks",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "social_network_id")
    )
    private List<SocialNetwork> connectedSocialNetworks;

    public User(String username, String password, String email, boolean isConfirmed,
        Date createdAt, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isConfirmed = isConfirmed;
        this.createdAt = createdAt;
        this.roles = roles;
    }

    public User(int id, String username, String email, String phoneNumber,
        Boolean isConfirmed, Date createdAt, Date timeOfSendingTheConfirmationLink,
        String activationCode, PersonalAccount personalAccount,
        List<Role> roles, List<SocialNetwork> unconnectedSocialNetworks) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isConfirmed = isConfirmed;
        this.createdAt = createdAt;
        this.timeOfSendingTheConfirmationLink = timeOfSendingTheConfirmationLink;
        this.activationCode = activationCode;
        this.personalAccount = personalAccount;
        this.roles = roles;
        this.unconnectedSocialNetworks = unconnectedSocialNetworks;
    }
}