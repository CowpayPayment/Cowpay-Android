package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_methods

import com.google.gson.annotations.SerializedName

data class GetMethodsResponse(
     internal val methodsList: Array<Int>? = null,
)