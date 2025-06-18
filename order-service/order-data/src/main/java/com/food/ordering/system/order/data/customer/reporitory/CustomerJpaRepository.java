package com.food.ordering.system.order.data.customer.reporitory;

import com.food.ordering.system.order.data.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

}
