package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.usecases

import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_token.GetTokenRequest
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.repository.PaymentMethodsRepository
import com.luminsoft.cowpay_sdk.utils.UseCase

class GetTokenUseCase(private  val paymentMethodsRepository: PaymentMethodsRepository): UseCase<Either<SdkFailure, String>, GetTokenUseCaseParams> {

    override suspend fun call(params: GetTokenUseCaseParams): Either<SdkFailure, String> {
       val request = GetTokenRequest()
        request.clientId = "M_${params.merchantCode}"
        request.secret = "${params.merchantCode}${params.merchantPhoneNumber}"
       return paymentMethodsRepository.getToken(request)
    }
}

data class GetTokenUseCaseParams(
    val merchantCode:String,
    val merchantPhoneNumber:String,
    )