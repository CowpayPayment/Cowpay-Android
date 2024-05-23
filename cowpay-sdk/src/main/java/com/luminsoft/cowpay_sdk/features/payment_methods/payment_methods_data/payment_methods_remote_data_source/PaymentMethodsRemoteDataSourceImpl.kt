package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_remote_data_source

import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_methods.GetMethodsRequest
import com.luminsoft.cowpay_sdk.network.BaseRemoteDataSource
import com.luminsoft.cowpay_sdk.network.BaseResponse
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_token.GetTokenRequest
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_api.PaymentMethodsApi
import com.luminsoft.cowpay_sdk.sdk.CowpaySDK


class PaymentMethodsRemoteDataSourceImpl(
    private val network: BaseRemoteDataSource,
    private val paymentMethodsApi: PaymentMethodsApi
) :
    PaymentMethodsRemoteDataSource {

    override suspend fun getToken(request: GetTokenRequest): BaseResponse<Any> {

        return network.apiRequest {
            paymentMethodsApi.getToken(
                request,
                CowpaySDK.environment.getTokenUrl()
            )
        }

    }

    override suspend fun getMethods(request: GetMethodsRequest): BaseResponse<Any> {

        return network.apiRequest { paymentMethodsApi.getMethods(request) }

    }
}






