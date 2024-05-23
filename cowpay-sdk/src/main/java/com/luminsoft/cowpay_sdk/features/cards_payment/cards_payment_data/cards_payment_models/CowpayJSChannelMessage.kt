package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models

import com.google.gson.annotations.SerializedName

data class CowpayJSChannelMessage(
    @SerializedName("orderId") internal val orderId: String? = "",
    @SerializedName("orderNumber") internal val orderNumber: String? = "",
    @SerializedName("merchantRedirectUrl") internal val merchantRedirectUrl: String? = "",
    @SerializedName("status") internal val paymentStatus: Int? = 0
) {
    fun parseCowpayJSChannelMessageStatusEnum(): CowpayJSChannelMessageStatusEnum {
        return when (this.paymentStatus) {
            2 -> CowpayJSChannelMessageStatusEnum.Success
            5 -> CowpayJSChannelMessageStatusEnum.Failed
            else -> {
                CowpayJSChannelMessageStatusEnum.Failed
            }
        }
    }
}

enum class CowpayJSChannelMessageStatusEnum {
    Success,
    Failed
}