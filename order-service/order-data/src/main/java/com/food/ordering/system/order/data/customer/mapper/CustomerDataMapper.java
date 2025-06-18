package com.food.ordering.system.order.data.customer.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.order.data.customer.entity.CustomerEntity;
import com.food.ordering.system.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataMapper {

    public Customer toCustomer(CustomerEntity entity) {
        return new Customer(new CustomerId(entity.getId()));
    }
}
