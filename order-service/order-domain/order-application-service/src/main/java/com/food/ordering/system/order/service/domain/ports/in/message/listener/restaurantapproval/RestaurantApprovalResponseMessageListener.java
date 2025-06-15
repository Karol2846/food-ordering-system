package com.food.ordering.system.order.service.domain.ports.in.message.listener.restaurantapproval;

import com.food.ordering.system.order.service.domain.dto.message.RestaurantApproveResponse;

public interface RestaurantApprovalResponseMessageListener {

    void orderApproved(RestaurantApproveResponse restaurantApproveResponse);

    void orderRejected(RestaurantApproveResponse restaurantApproveResponse);
}
