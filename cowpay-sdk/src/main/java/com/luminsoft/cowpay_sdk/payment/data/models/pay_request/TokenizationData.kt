package com.luminsoft.cowpay_sdk.payment.data.models.pay_request

data class TokenizationData (
    val tokenId: String? = null,
    val cvv: String? = null,
    val returnUrl3DS: String? = null,
)