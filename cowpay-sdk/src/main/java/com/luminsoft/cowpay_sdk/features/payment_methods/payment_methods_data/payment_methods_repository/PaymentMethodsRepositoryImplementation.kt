package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_repository


import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_methods.GetMethodsRequest
import com.luminsoft.cowpay_sdk.network.BaseResponse
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_token.GetTokenRequest
import com.luminsoft.cowpay_sdk.network.ApiBaseResponse
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_remote_data_source.PaymentMethodsRemoteDataSource
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.repository.PaymentMethodsRepository

class PaymentMethodsRepositoryImplementation( private val paymentMethodsRemoteDataSource: PaymentMethodsRemoteDataSource):
    PaymentMethodsRepository {

    override suspend fun getToken(request: GetTokenRequest): Either<SdkFailure, String> {
        return when (val response = paymentMethodsRemoteDataSource.getToken(request)) {
            is BaseResponse.Success -> {
                Either.Right(response.data as String)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
    override suspend fun getMethods(request: GetMethodsRequest): Either<SdkFailure, Array<Int>> {
        return when (val response = paymentMethodsRemoteDataSource.getMethods(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<Array<Int>>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

