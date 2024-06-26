package com.luminsoft.cowpay_sdk.network

import com.luminsoft.cowpay_sdk.failures.SdkFailure

sealed  class BaseResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : BaseResponse<T>()
    data class Error(val error: SdkFailure) : BaseResponse<SdkFailure>()
}
