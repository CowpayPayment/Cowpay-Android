package com.luminsoft.cowpay_sdk.payment.domain.usecases

import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.payment.data.models.PaymentOption
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesRequest
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesResponse
import com.luminsoft.cowpay_sdk.payment.data.models.setPaymentOption
import com.luminsoft.cowpay_sdk.payment.domain.repository.PaymentRepository
import com.luminsoft.cowpay_sdk.utils.UseCase

class GetFeesUseCase(private  val paymentRepository: PaymentRepository): UseCase<Either<SdkFailure, GetFeesResponse>, GetFeesUseCaseParams> {

    override suspend fun call(params: GetFeesUseCaseParams): Either<SdkFailure, GetFeesResponse> {
       var request = GetFeesRequest()
        request.merchantCode = params.merchantCode
        request.amount = params.amount
        request.paymentMethodTypeId = params.paymentMethodTypeId

       return paymentRepository.getFees(request)
    }
}


data class GetFeesUseCaseParams(
    val merchantCode:String,
    val amount:Number,
    val paymentMethodTypeId:Int,
)