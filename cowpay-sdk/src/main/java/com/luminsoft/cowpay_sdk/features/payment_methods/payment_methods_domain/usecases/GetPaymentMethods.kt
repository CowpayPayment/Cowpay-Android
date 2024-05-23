package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.usecases

import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_methods.GetMethodsRequest
import com.luminsoft.cowpay_sdk.payment.data.models.PaymentOption
import com.luminsoft.cowpay_sdk.payment.data.models.setPaymentOption
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.repository.PaymentMethodsRepository
import com.luminsoft.cowpay_sdk.utils.UseCase

class GetPaymentMethodsUseCase(private  val paymentMethodsRepository: PaymentMethodsRepository): UseCase<Either<SdkFailure, ArrayList<PaymentOption>?>, GetPaymentMethodsUseCaseParams> {

    override suspend fun call(params: GetPaymentMethodsUseCaseParams): Either<SdkFailure, ArrayList<PaymentOption>?> {
       val request = GetMethodsRequest()
        request.merchantCode = params.merchantCode
        val paymentOptionsList:ArrayList<PaymentOption> = ArrayList()
         paymentMethodsRepository.getMethods(request).fold(
            {
                return Either.Left(it)
            },
            { s ->
                s.let { it ->
                    it!!.forEach { it1 ->
                        setPaymentOption(it1).let {
                            if(it != null) {
                                paymentOptionsList.add(it)
                            }
                        }
                    }
                    return Either.Right(paymentOptionsList)
                }
            })
    }
}


data class GetPaymentMethodsUseCaseParams(
    val merchantCode:String,
)