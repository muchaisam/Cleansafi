package com.cleansafi.data.mapper

import com.cleansafi.data.local.entity.CartItemEntity
import com.cleansafi.domain.model.CartItem
import com.cleansafi.domain.model.LaundryItemType
import com.cleansafi.domain.model.ServiceType

fun CartItemEntity.toDomain(): CartItem {
    return CartItem(
        itemType = LaundryItemType.valueOf(itemType),
        quantity = quantity,
        serviceType = ServiceType.valueOf(serviceType),
        pricePerItem = pricePerItem
    )
}

fun CartItem.toEntity(): CartItemEntity {
    return CartItemEntity(
        itemType = itemType.name,
        quantity = quantity,
        serviceType = serviceType.name,
        pricePerItem = pricePerItem
    )
}
