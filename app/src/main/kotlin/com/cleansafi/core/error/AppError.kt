package com.cleansafi.core.error

sealed class AppError(
    open val message: String,
    open val cause: Throwable? = null
) {
    data class NetworkError(
        override val message: String = "No internet connection",
        override val cause: Throwable? = null
    ) : AppError(message, cause)
    
    data class DatabaseError(
        override val message: String = "Failed to access local data",
        override val cause: Throwable? = null
    ) : AppError(message, cause)
    
    data class ValidationError(
        override val message: String = "Invalid input",
        val field: String? = null,
        override val cause: Throwable? = null
    ) : AppError(message, cause)
    
    data class AuthenticationError(
        override val message: String = "Authentication failed",
        override val cause: Throwable? = null
    ) : AppError(message, cause)
    
    data class PaymentError(
        override val message: String = "Payment failed",
        override val cause: Throwable? = null
    ) : AppError(message, cause)
    
    data class ServerError(
        override val message: String = "Server error occurred",
        val code: Int? = null,
        override val cause: Throwable? = null
    ) : AppError(message, cause)
    
    data class UnknownError(
        override val message: String = "An unexpected error occurred",
        override val cause: Throwable? = null
    ) : AppError(message, cause)
}

fun Throwable.toAppError(): AppError {
    return when (this) {
        is java.net.UnknownHostException,
        is java.net.SocketTimeoutException,
        is java.io.IOException -> AppError.NetworkError(
            message = "Please check your internet connection",
            cause = this
        )
        is IllegalArgumentException -> AppError.ValidationError(
            message = this.message ?: "Invalid input",
            cause = this
        )
        else -> AppError.UnknownError(
            message = this.message ?: "Something went wrong",
            cause = this
        )
    }
}
