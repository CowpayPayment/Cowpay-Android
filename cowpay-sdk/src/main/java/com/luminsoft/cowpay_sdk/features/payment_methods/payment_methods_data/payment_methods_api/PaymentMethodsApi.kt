package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_api


import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_methods.GetMethodsRequest
import com.luminsoft.cowpay_sdk.network.ApiBaseResponse
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_token.GetTokenRequest
import retrofit2.Response
import retrofit2.http.*

interface PaymentMethodsApi {
    @POST
    suspend fun getToken(@Body request: GetTokenRequest, @Url url: String): Response<String>

    @POST("/payment/GetMerchantPaymentMethods")
    suspend fun getMethods(@Body request: GetMethodsRequest): Response<ApiBaseResponse<Array<Int>>>
}