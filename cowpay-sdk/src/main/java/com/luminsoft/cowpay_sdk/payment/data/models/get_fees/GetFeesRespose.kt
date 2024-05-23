package com.luminsoft.cowpay_sdk.payment.data.models.get_fees

import com.google.gson.annotations.SerializedName

data class GetFeesResponse(
     @SerializedName("feesValue") internal val feesValue: Number?,
     @SerializedName("vatValue") internal val vatValue: Number? ,
     @SerializedName("hasFees") internal val hasFees: Boolean? ,
)