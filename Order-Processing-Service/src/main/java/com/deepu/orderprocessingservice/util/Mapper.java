package com.deepu.orderprocessingservice.util;

import com.deepu.orderprocessingservice.entity.Order;
import com.deepu.orderprocessingservice.response.OrderObject;

public class Mapper {

    public static OrderObject convertToOrderObject(Order order) {
        OrderObject orderObject = new OrderObject();
        orderObject.setUniqueId(order.getUniqueId());
        orderObject.setStatus(order.getStatus());
        orderObject.setQuantity(order.getQuantity());
        orderObject.setProductPrice(order.getProductPrice());
        orderObject.setProductId(order.getProductId());
        orderObject.setOrderId(order.getOrderId());
        orderObject.setOrderDate(order.getOrderDate());
        orderObject.setHandlingFee(order.getHandlingFee());
        orderObject.setEmail(order.getEmail());
        orderObject.setDeliveryCharge(order.getDeliveryCharge());
        orderObject.setDeliverDate(order.getDeliverDate());
        orderObject.setAddressID(order.getAddressID());
        orderObject.setCartId(order.getCartId());
        return orderObject;
    }
}
