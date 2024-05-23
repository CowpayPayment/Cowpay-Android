package com.luminsoft.cowpay_sdk.payment.data.models.get_fees

import com.google.gson.annotations.SerializedName

open class GetFeesRequest {
    @SerializedName("merchantCode")
    internal var merchantCode: String? = null
    @SerializedName("amount")
    internal var amount: Number? = null
    @SerializedName("paymentMethodType")
    internal var paymentMethodTypeId: Int? = null
}
