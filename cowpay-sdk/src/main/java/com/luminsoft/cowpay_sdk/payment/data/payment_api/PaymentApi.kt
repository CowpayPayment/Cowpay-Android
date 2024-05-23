package com.luminsoft.cowpay_sdk.payment.data.payment_api


import com.luminsoft.cowpay_sdk.network.ApiBaseResponse
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesRequest
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesResponse
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayRequest
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayResponse

import retrofit2.Response
import retrofit2.http.*

interface PaymentApi {
    @POST("/payment/Pay")
    suspend fun pay(@Body request : PayRequest): Response<ApiBaseResponse<PayResponse>>
    @POST("/payment/CalculateFees")
    suspend fun getFees(@Body request : GetFeesRequest): Response<ApiBaseResponse<GetFeesResponse>>
}