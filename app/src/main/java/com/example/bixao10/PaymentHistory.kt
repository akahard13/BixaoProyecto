package com.example.bixao10

data class PaymentHistory(
    val payments: MutableList<Payment> = mutableListOf()
)
