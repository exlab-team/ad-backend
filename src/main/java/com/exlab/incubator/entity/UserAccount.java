package com.exlab.incubator.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Entity
@Table(name = "user_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tariff_id", referencedColumnName = "id")
    private Tariff tariff;

    @Column(length = 100)
    private String personalAccount;

    @Column(name = "modified_at")
    private Instant modifiedAt;

    @ManyToMany
    @JoinTable(
        name = "user_accounts_social_networks",
        joinColumns = @JoinColumn(name = "user_account_id"),
        inverseJoinColumns = @JoinColumn(name = "social_network_id")
    )
    @Exclude
    @Builder.Default
    private List<SocialNetwork> socialNetworks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "user_accounts_advisory_materials",
        joinColumns = @JoinColumn(name = "user_account_id"),
        inverseJoinColumns = @JoinColumn(name = "advisory_material_id")
    )
    @Exclude
    @Builder.Default
    private List<AdvisoryMaterial> advisoryMaterials = new ArrayList<>();
}
