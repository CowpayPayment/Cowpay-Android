package com.luminsoft.cowpay_sdk.payment.data.remote_data_source
import com.luminsoft.cowpay_sdk.network.BaseRemoteDataSource
import com.luminsoft.cowpay_sdk.network.BaseResponse
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesRequest
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayRequest
import com.luminsoft.cowpay_sdk.payment.data.payment_api.PaymentApi


class  PaymentRemoteDataSourceImpl (private val network: BaseRemoteDataSource, private val paymentApi: PaymentApi):
    PaymentRemoteDataSource {

    override suspend fun pay(request: PayRequest, ): BaseResponse<Any> {

            return network.apiRequest { paymentApi.pay(request) }

    }
    override suspend fun getFees(request: GetFeesRequest, ): BaseResponse<Any> {

            return network.apiRequest { paymentApi.getFees(request) }

    }
}






