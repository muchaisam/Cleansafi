package com.cleansafi.presentation.order.placeorder

import com.cleansafi.domain.model.LaundryItemType
import com.cleansafi.domain.model.ServiceType

object PricingCalculator {

    fun calculateItemPrice(itemType: LaundryItemType, serviceType: ServiceType): Int {
        return when (serviceType) {
            ServiceType.IRON_ONLY -> when (itemType) {
                LaundryItemType.TOPS -> 30
                LaundryItemType.TROUSERS -> 40
                LaundryItemType.BEDSHEETS -> 50
                LaundryItemType.OTHERS -> 60
            }

            ServiceType.WASH_ONLY -> when (itemType) {
                LaundryItemType.TOPS -> 40
                LaundryItemType.TROUSERS -> 50
                LaundryItemType.BEDSHEETS -> 60
                LaundryItemType.OTHERS -> 70
            }

            ServiceType.WASH_AND_IRON -> when (itemType) {
                LaundryItemType.TOPS -> 50
                LaundryItemType.TROUSERS -> 60
                LaundryItemType.BEDSHEETS -> 70
                LaundryItemType.OTHERS -> 80
            }
        }
    }

    fun calculateTotalPrice(
        itemQuantities: Map<LaundryItemType, Int>,
        serviceType: ServiceType
    ): Int {
        return itemQuantities.entries.sumOf { (itemType, quantity) ->
            calculateItemPrice(itemType, serviceType) * quantity
        }
    }
}
