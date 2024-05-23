package com.luminsoft.cowpay_sdk.sdk.model


interface CowpayCallback {
    fun success(paymentSuccessModel: PaymentSuccessModel)
    fun error(paymentFailedModel: PaymentFailedModel)
    fun closedByUser()
}
