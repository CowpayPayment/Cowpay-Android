package com.luminsoft.cowpay_sdk.payment.data.remote_data_source

import com.luminsoft.cowpay_sdk.network.BaseResponse
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesRequest
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayRequest

interface  PaymentRemoteDataSource  {
    suspend fun pay(request: PayRequest):BaseResponse<Any>
    suspend fun getFees(request: GetFeesRequest):BaseResponse<Any>
}