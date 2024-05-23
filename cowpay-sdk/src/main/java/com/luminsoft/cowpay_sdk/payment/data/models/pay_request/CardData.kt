package com.luminsoft.cowpay_sdk.payment.data.models.pay_request

data class CardData (
    val cardNumber: String? = null,
    val cvv: String? = null,
    val expiryDate: String? = null,
    val cardHolder: String? = null,
    val returnUrl3DS: String? = null,
    val isTokenized: Boolean? = null,
)