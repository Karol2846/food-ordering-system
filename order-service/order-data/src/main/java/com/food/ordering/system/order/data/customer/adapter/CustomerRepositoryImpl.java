package com.food.ordering.system.order.data.customer.adapter;

import com.food.ordering.system.order.data.customer.mapper.CustomerDataMapper;
import com.food.ordering.system.order.data.customer.reporitory.CustomerJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.ports.out.reporitory.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerDataMapper mapper;
    private final CustomerJpaRepository repository;

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return repository.findById(customerId).map(mapper::toCustomer);
    }
}
