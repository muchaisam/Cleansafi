package com.cleansafi.data.local

import androidx.room.TypeConverter
import com.cleansafi.domain.model.LaundryItemType
import com.cleansafi.domain.model.OrderStatus
import com.cleansafi.domain.model.ServiceType

class Converters {
    @TypeConverter
    fun fromServiceType(value: ServiceType): String = value.name
    
    @TypeConverter
    fun toServiceType(value: String): ServiceType = ServiceType.valueOf(value)
    
    @TypeConverter
    fun fromLaundryItemType(value: LaundryItemType): String = value.name
    
    @TypeConverter
    fun toLaundryItemType(value: String): LaundryItemType = LaundryItemType.valueOf(value)
    
    @TypeConverter
    fun fromOrderStatus(value: OrderStatus): String = value.name
    
    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus = OrderStatus.valueOf(value)
}
