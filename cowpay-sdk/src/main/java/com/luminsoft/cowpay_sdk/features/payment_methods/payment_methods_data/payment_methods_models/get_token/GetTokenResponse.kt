package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_token

import com.google.gson.annotations.SerializedName

data class GetTokenResponse(
    @SerializedName("payment_gateway_reference_id") internal val paymentGatewayReferenceId: String? = "",
    @SerializedName("merchant_reference_id") internal val merchantReferenceId: String? = "",
    @SerializedName("cowpay_reference_id") internal val cowpayReferenceId: String? = "",
    @SerializedName("token") internal val token: String? = "",
    @SerializedName("payment_status") internal val paymentStatus: String? = "",
    @SerializedName("message_type") internal val message: String? = ""
)