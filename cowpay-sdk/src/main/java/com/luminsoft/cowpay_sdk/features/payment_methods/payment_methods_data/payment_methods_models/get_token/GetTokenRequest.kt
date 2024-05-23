package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_token

import com.google.gson.annotations.SerializedName

open class GetTokenRequest {
    @SerializedName("clientId")
    internal var clientId: String? = null

    @SerializedName("secret")
    internal var secret: String? = null

}
