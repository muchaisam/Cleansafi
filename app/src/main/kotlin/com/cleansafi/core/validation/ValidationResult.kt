package com.cleansafi.core.validation

sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()

    val isValid: Boolean
        get() = this is Valid

    val errorMessage: String?
        get() = (this as? Invalid)?.message
}
