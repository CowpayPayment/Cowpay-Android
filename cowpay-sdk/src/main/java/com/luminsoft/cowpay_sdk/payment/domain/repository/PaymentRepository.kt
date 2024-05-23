package com.luminsoft.cowpay_sdk.payment.domain.repository

import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesRequest
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesResponse
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayRequest
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayResponse

interface PaymentRepository {
     suspend fun pay(request: PayRequest): Either<SdkFailure, PayResponse>
     suspend fun getFees(request: GetFeesRequest): Either<SdkFailure, GetFeesResponse>
}