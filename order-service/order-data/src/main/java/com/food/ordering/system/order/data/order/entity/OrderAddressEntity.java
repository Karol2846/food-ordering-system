package com.food.ordering.system.order.data.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "order_address")
public class OrderAddressEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    private String postalCode;
    private String street;
    private String city;
}
