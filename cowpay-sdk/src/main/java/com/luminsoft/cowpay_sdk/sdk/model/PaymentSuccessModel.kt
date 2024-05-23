package com.luminsoft.cowpay_sdk.sdk.model

import com.luminsoft.cowpay_sdk.network.ApiErrorResponse

sealed class PaymentSuccessModel {
    class CreditCardSuccessModel(paymentReferenceId: String) : PaymentSuccessModel(
        "CreditCard",
        paymentReferenceId,
    )
    class BankCardSuccessModel(paymentReferenceId: String) : PaymentSuccessModel(
        "Bank Card",
        paymentReferenceId,
    )

    class FawrySuccessModel(paymentReferenceId: String, val fawryCode: String) :
        PaymentSuccessModel("Fawry", paymentReferenceId)

    val paymentMethodName: String
    val paymentReferenceId: String

    constructor(paymentMethodName: String, paymentReferenceId: String) {
        this.paymentMethodName = paymentMethodName
        this.paymentReferenceId = paymentReferenceId
    }
}
