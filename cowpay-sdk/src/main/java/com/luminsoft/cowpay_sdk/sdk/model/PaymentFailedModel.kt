package com.luminsoft.cowpay_sdk.sdk.model

import com.luminsoft.cowpay_sdk.network.ApiErrorResponse

data class PaymentFailedModel (val  failureMessage:String,val error:Any? = null)