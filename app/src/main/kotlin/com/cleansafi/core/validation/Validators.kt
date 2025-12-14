package com.cleansafi.core.validation

import android.util.Patterns

object Validators {

    // Email validation
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Invalid("Email is required")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                ValidationResult.Invalid("Invalid email format")

            else -> ValidationResult.Valid
        }
    }

    // Password validation - minimum 8 characters, at least one uppercase, one lowercase, one digit
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Invalid("Password is required")
            password.length < 8 -> ValidationResult.Invalid("Password must be at least 8 characters")
            !password.any { it.isUpperCase() } ->
                ValidationResult.Invalid("Password must contain at least one uppercase letter")

            !password.any { it.isLowerCase() } ->
                ValidationResult.Invalid("Password must contain at least one lowercase letter")

            !password.any { it.isDigit() } ->
                ValidationResult.Invalid("Password must contain at least one digit")

            else -> ValidationResult.Valid
        }
    }

    // Password confirmation validation
    fun validatePasswordConfirmation(password: String, confirmPassword: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult.Invalid("Please confirm your password")
            password != confirmPassword -> ValidationResult.Invalid("Passwords do not match")
            else -> ValidationResult.Valid
        }
    }

    // Kenyan phone number validation (07XX XXX XXX or 01XX XXX XXX or +254...)
    fun validatePhoneNumber(phone: String): ValidationResult {
        val cleanedPhone = phone.replace("\\s".toRegex(), "")
        return when {
            cleanedPhone.isBlank() -> ValidationResult.Invalid("Phone number is required")
            cleanedPhone.startsWith("+254") -> {
                if (cleanedPhone.length != 13) {
                    ValidationResult.Invalid("Invalid Kenyan phone number (+254XXXXXXXXX)")
                } else ValidationResult.Valid
            }

            cleanedPhone.startsWith("0") -> {
                if (cleanedPhone.length != 10) {
                    ValidationResult.Invalid("Invalid phone number (10 digits required)")
                } else ValidationResult.Valid
            }

            cleanedPhone.startsWith("254") -> {
                if (cleanedPhone.length != 12) {
                    ValidationResult.Invalid("Invalid Kenyan phone number (254XXXXXXXXX)")
                } else ValidationResult.Valid
            }

            else -> ValidationResult.Invalid("Phone number must start with 07, 01, or +254")
        }
    }

    // Name validation
    fun validateName(name: String, fieldName: String = "Name"): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Invalid("$fieldName is required")
            name.length < 2 -> ValidationResult.Invalid("$fieldName must be at least 2 characters")
            !name.all { it.isLetter() || it.isWhitespace() } ->
                ValidationResult.Invalid("$fieldName must contain only letters")

            else -> ValidationResult.Valid
        }
    }

    // M-PESA phone number validation (must start with 254)
    fun validateMpesaPhoneNumber(phone: String): ValidationResult {
        val cleanedPhone = phone.replace("\\s".toRegex(), "")
        return when {
            cleanedPhone.isBlank() -> ValidationResult.Invalid("M-PESA phone number is required")
            !cleanedPhone.startsWith("254") ->
                ValidationResult.Invalid("M-PESA number must start with 254 (e.g., 254712345678)")

            cleanedPhone.length != 12 ->
                ValidationResult.Invalid("M-PESA number must be 12 digits (254XXXXXXXXX)")

            !cleanedPhone.substring(3).all { it.isDigit() } ->
                ValidationResult.Invalid("M-PESA number must contain only digits")

            else -> ValidationResult.Valid
        }
    }

    // Address validation
    fun validateAddress(address: String): ValidationResult {
        return when {
            address.isBlank() -> ValidationResult.Invalid("Address is required")
            address.length < 10 -> ValidationResult.Invalid("Please provide a complete address")
            else -> ValidationResult.Valid
        }
    }

    // Required field validation
    fun validateRequired(value: String, fieldName: String): ValidationResult {
        return when {
            value.isBlank() -> ValidationResult.Invalid("$fieldName is required")
            else -> ValidationResult.Valid
        }
    }

    // Sanitize input (prevent SQL injection, XSS - Room handles SQL, but good practice)
    fun sanitizeInput(input: String): String {
        return input.trim()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
}
