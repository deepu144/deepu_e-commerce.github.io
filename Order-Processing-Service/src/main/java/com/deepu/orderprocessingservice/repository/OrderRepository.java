package com.deepu.orderprocessingservice.repository;

import com.deepu.orderprocessingservice.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order,String> {
    Order findByOrderId(String orderId);
}
