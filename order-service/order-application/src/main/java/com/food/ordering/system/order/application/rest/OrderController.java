package com.food.ordering.system.order.application.rest;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.ports.in.service.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand request) {
        log.info("Creating order for customer: {} at restaurant: {}", request.customerId(), request.restaurantId());

        CreateOrderResponse orderResponse = orderApplicationService.createOrder(request);
        log.info("Order created with tracking id: {}", orderResponse.orderTrackingId());
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTRackingId(@PathVariable UUID trackingId) {
        log.info("Received order status with tracking id: {}", trackingId);

        return ResponseEntity.ok(orderApplicationService.tractOrder(
                TrackOrderQuery.builder()
                        .orderTrackingId(trackingId)
                        .build()));
    }
}
