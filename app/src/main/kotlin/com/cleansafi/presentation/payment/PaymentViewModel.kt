package com.cleansafi.presentation.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cleansafi.core.util.PreferencesManager
import com.cleansafi.core.validation.Validators
import com.cleansafi.domain.model.OrderStatus
import com.cleansafi.domain.model.PaymentStatus
import com.cleansafi.domain.repository.OrderStatusRepository
import com.cleansafi.domain.repository.PaymentRepository
import com.cleansafi.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
    private val orderStatusRepository: OrderStatusRepository,
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager,
    private val orderSimulationManager: com.cleansafi.core.simulation.OrderSimulationManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(PaymentState())
    val state = _state.asStateFlow()

    init {
        val orderId = savedStateHandle.get<Long>("orderId") ?: 0
        val amount = savedStateHandle.get<Int>("amount") ?: 0

        _state.update {
            it.copy(
                orderId = orderId,
                amount = amount
            )
        }

        loadUserPhone()
    }

    private fun loadUserPhone() {
        viewModelScope.launch {
            val userId = preferencesManager.currentUserId ?: return@launch
            val user = userRepository.getUserById(userId)

            _state.update { it.copy(phoneNumber = user?.phoneNumber ?: "") }
        }
    }

    fun onPhoneNumberChange(phone: String) {
        _state.update { it.copy(phoneNumber = phone) }
    }

    fun initiatePayment() {
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true, error = null) }

            try {
                // Validate M-PESA phone number
                val phoneValidation = Validators.validateMpesaPhoneNumber(_state.value.phoneNumber)
                if (!phoneValidation.isValid) {
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            error = phoneValidation.errorMessage
                        )
                    }
                    return@launch
                }

                // Initiate payment
                paymentRepository.initiatePayment(
                    orderId = _state.value.orderId,
                    amount = _state.value.amount,
                    phoneNumber = _state.value.phoneNumber
                ).onSuccess { payment ->
                    _state.update {
                        it.copy(
                            payment = payment,
                            isProcessing = true,
                            paymentStatus = PaymentStatus.PROCESSING
                        )
                    }

                    // Start countdown
                    startCountdown()

                    // Observe payment status
                    observePayment()
                }.onFailure { exception ->
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            error = exception.message ?: "Failed to initiate payment"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isProcessing = false,
                        error = e.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }


    private fun observePayment() {
        viewModelScope.launch {
            paymentRepository.observePayment(_state.value.orderId).collect { payment ->
                payment?.let {
                    _state.update { state ->
                        state.copy(
                            payment = it,
                            paymentStatus = it.status,
                            isProcessing = it.status == PaymentStatus.PROCESSING,
                            isSuccess = it.status == PaymentStatus.SUCCESS
                        )
                    }

                    // Update order status on successful payment
                    if (it.status == PaymentStatus.SUCCESS) {
                        orderStatusRepository.updateOrderStatus(
                            orderId = _state.value.orderId,
                            status = OrderStatus.CONFIRMED,
                            note = "Payment successful - ${it.transactionId}"
                        )

                        // Start automatic order lifecycle simulation
                        orderSimulationManager.scheduleOrderProgression(_state.value.orderId)
                    } else if (it.status == PaymentStatus.FAILED) {
                        orderStatusRepository.updateOrderStatus(
                            orderId = _state.value.orderId,
                            status = OrderStatus.PAYMENT_FAILED,
                            note = "Payment failed - ${it.failureReason}"
                        )
                    }
                }
            }
        }
    }

    private fun startCountdown() {
        viewModelScope.launch {
            for (i in 60 downTo 0) {
                _state.update { it.copy(countdown = i) }
                delay(1000)

                // Stop countdown if payment completed
                if (_state.value.paymentStatus != PaymentStatus.PROCESSING) {
                    break
                }
            }

            // Auto-fail if countdown reaches 0
            if (_state.value.paymentStatus == PaymentStatus.PROCESSING) {
                simulateFailure("Payment timeout - no response received")
            }
        }
    }

    // Demo controls for portfolio
    fun simulateSuccess() {
        viewModelScope.launch {
            val payment = _state.value.payment ?: return@launch
            val transactionId = "TX${UUID.randomUUID().toString().take(10).uppercase()}"

            paymentRepository.simulatePaymentSuccess(
                paymentId = payment.paymentId,
                transactionId = transactionId
            )
        }
    }

    fun simulateFailure(reason: String = "Insufficient funds") {
        viewModelScope.launch {
            val payment = _state.value.payment ?: return@launch

            paymentRepository.simulatePaymentFailure(
                paymentId = payment.paymentId,
                reason = reason
            )
        }
    }

    private fun isValidKenyanPhone(phone: String): Boolean {
        val cleaned = phone.replace(Regex("[^0-9]"), "")
        return cleaned.matches(Regex("^(254|0)[17]\\d{8}$"))
    }
}
