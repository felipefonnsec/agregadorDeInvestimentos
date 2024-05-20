package com.project.agregadorinvestimentos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tb_billingaddress")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillingAddress {

    @Id
    @Column(name = "acoount_id")
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "accountId")
    private Account account;

    @Column(name = "street")
    private String street;

    @Column(name = "number")
    private Integer number;
}
