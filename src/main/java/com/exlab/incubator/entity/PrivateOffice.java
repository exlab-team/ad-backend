package com.exlab.incubator.entity;

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
@Table(name = "private_offices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 30, name = "tariff")
    private String tariff;

    @OneToOne
    @JoinColumn(name = "personal_account_id", referencedColumnName = "id")
    private PersonalAccount personalAccount;

    @ManyToMany
    @JoinTable(
        name = "private_offices_social_networks",
        joinColumns = @JoinColumn(name = "private_office_id"),
        inverseJoinColumns = @JoinColumn(name = "social_network_id")
    )
    private List<SocialNetwork> socialNetworks;

    @ManyToMany
    @JoinTable(
        name = "private_offices_advisory_materials",
        joinColumns = @JoinColumn(name = "private_office_id"),
        inverseJoinColumns = @JoinColumn(name = "advisory_material_id")
    )
    private List<AdvisoryMaterial> advisoryMaterials;
}
