package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.repository

import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_methods.GetMethodsRequest
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_models.get_token.GetTokenRequest

interface PaymentMethodsRepository {
     suspend fun getToken(request: GetTokenRequest): Either<SdkFailure, String>
     suspend fun getMethods(request: GetMethodsRequest): Either<SdkFailure, Array<Int>?>
}