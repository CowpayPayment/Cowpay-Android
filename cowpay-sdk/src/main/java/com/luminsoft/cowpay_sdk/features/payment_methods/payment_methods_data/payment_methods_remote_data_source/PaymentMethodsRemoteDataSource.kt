package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_remote_data_source

import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_methods.GetMethodsRequest
import com.luminsoft.cowpay_sdk.network.BaseResponse
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_token.GetTokenRequest

interface  PaymentMethodsRemoteDataSource  {
    suspend fun getToken(request: GetTokenRequest):BaseResponse<Any>
    suspend fun getMethods(request: GetMethodsRequest):BaseResponse<Any>
}