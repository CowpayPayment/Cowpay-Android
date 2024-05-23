package com.luminsoft.cowpay_sdk.payment.data.payment_repository


import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.network.ApiBaseResponse
import com.luminsoft.cowpay_sdk.network.BaseResponse
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesRequest
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesResponse
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayRequest
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayResponse
import com.luminsoft.cowpay_sdk.payment.data.remote_data_source.PaymentRemoteDataSource
import com.luminsoft.cowpay_sdk.payment.domain.repository.PaymentRepository

class PaymentRepositoryImplementation( private val paymentRemoteDataSource: PaymentRemoteDataSource):PaymentRepository {

    override suspend fun pay(request: PayRequest): Either<SdkFailure, PayResponse> {
        return when (val response = paymentRemoteDataSource.pay(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<PayResponse>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
    override suspend fun getFees(request: GetFeesRequest): Either<SdkFailure, GetFeesResponse> {
        return when (val response = paymentRemoteDataSource.getFees(request)) {
            is BaseResponse.Success -> {
                Either.Right((response.data as ApiBaseResponse<GetFeesResponse>).data)
            }

            is BaseResponse.Error -> {
                Either.Left(response.error)
            }

        }
    }
}

