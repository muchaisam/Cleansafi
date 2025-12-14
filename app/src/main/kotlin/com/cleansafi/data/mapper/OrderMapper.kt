package com.cleansafi.data.mapper

import com.cleansafi.data.local.dao.OrderWithItems
import com.cleansafi.data.local.entity.OrderEntity
import com.cleansafi.data.local.entity.OrderItemEntity
import com.cleansafi.domain.model.LaundryItemType
import com.cleansafi.domain.model.Order
import com.cleansafi.domain.model.OrderItem
import com.cleansafi.domain.model.OrderStatus
import com.cleansafi.domain.model.ServiceType

fun OrderWithItems.toDomain(): Order {
    return Order(
        orderId = order.orderId,
        userId = order.userId,
        items = items.map { it.toDomain() },
        totalPrice = order.totalPrice,
        status = OrderStatus.valueOf(order.status),
        scheduledDate = order.scheduledDate,
        scheduledTime = order.scheduledTime,
        createdAt = order.createdAt
    )
}

fun OrderItemEntity.toDomain(): OrderItem {
    return OrderItem(
        itemId = itemId,
        orderId = orderId,
        itemType = LaundryItemType.valueOf(itemType),
        quantity = quantity,
        serviceType = ServiceType.valueOf(serviceType),
        pricePerItem = pricePerItem
    )
}

fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        orderId = orderId,
        userId = userId,
        totalPrice = totalPrice,
        status = status.name,
        scheduledDate = scheduledDate,
        scheduledTime = scheduledTime,
        createdAt = createdAt
    )
}

fun OrderItem.toEntity(): OrderItemEntity {
    return OrderItemEntity(
        itemId = itemId,
        orderId = orderId,
        itemType = itemType.name,
        quantity = quantity,
        serviceType = serviceType.name,
        pricePerItem = pricePerItem
    )
}
