package com.cleansafi.domain.model

enum class ServiceType {
    WASH_ONLY,
    IRON_ONLY,
    WASH_AND_IRON
}

enum class LaundryItemType(val displayName: String) {
    TOPS("Tops & Shirts"),
    TROUSERS("Trousers & Jeans"),
    BEDSHEETS("Bedsheets"),
    OTHERS("Others (Towels, etc.)")
}

enum class OrderStatus(
    val displayName: String,
    val description: String,
    val emoji: String
) {
    PENDING("Pending Payment", "Awaiting payment confirmation", "⏳"),
    PAYMENT_FAILED("Payment Failed", "Payment was unsuccessful", "❌"),
    CONFIRMED("Confirmed", "Payment successful, preparing pickup", "✅"),
    PICKED_UP("Picked Up", "Your laundry has been collected", "🚚"),
    PROCESSING("Processing", "Your items are being washed/ironed", "🧺"),
    READY("Ready", "Your laundry is ready for delivery", "✨"),
    OUT_FOR_DELIVERY("Out for Delivery", "Driver is on the way", "🚗"),
    DELIVERED("Delivered", "Successfully delivered to you", "📦"),
    COMPLETED("Completed", "Order completed", "🎉"),
    CANCELLED("Cancelled", "Order was cancelled", "🚫");
    
    val isActive: Boolean
        get() = this !in listOf(COMPLETED, CANCELLED, PAYMENT_FAILED)
    
    val canProgress: Boolean
        get() = this in listOf(PENDING, CONFIRMED, PICKED_UP, PROCESSING, READY, OUT_FOR_DELIVERY, DELIVERED)
    
    fun nextStatus(): OrderStatus? = when (this) {
        PENDING -> CONFIRMED
        CONFIRMED -> PICKED_UP
        PICKED_UP -> PROCESSING
        PROCESSING -> READY
        READY -> OUT_FOR_DELIVERY
        OUT_FOR_DELIVERY -> DELIVERED
        DELIVERED -> COMPLETED
        else -> null
    }
}

enum class PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    CANCELLED
}
