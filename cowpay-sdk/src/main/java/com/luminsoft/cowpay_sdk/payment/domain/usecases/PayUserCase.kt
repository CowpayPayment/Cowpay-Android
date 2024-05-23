package com.luminsoft.cowpay_sdk.payment.domain.usecases

import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.payment.data.models.PaymentOption
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.CardData
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayRequest
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayResponse
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.TokenizationData
import com.luminsoft.cowpay_sdk.payment.domain.repository.PaymentRepository
import com.luminsoft.cowpay_sdk.utils.UseCase

class PayUseCase(private val paymentRepository: PaymentRepository) :
    UseCase<Either<SdkFailure, PayResponse>, PayUseCaseParams> {

    override suspend fun call(params: PayUseCaseParams): Either<SdkFailure, PayResponse> {
        val request = PayRequest()

        request.gatewayTargetMethod = params.paymentOption?.getGatewayTargetMethod()
        request.merchantReferenceId = params.merchantReferenceId
        request.customerMerchantProfileId = params.customerMerchantProfileId
        request.customerFirstName = params.customerFirstName
        request.customerLastName = params.customerFirstName
        request.customerEmail = params.customerEmail
        request.customerMobile = params.customerMobile
        request.amount = params.amount
        request.signature = params.signature
        request.description = params.description
        request.isFeesOnCustomer = params.isFeesOnCustomer

        if (params.paymentOption == PaymentOption.CreditCard && params.cardData != null) {
            request.cardNumber = params.cardData.cardNumber
            request.cvv = params.cardData.cvv
            request.expiryYear = "20${params.cardData.expiryDate!!.split("/")[1]}"
            request.expiryMonth = params.cardData.expiryDate.split("/")[0]
            request.cardHolder = params.cardData.cardHolder
            request.returnUrl3DS = params.cardData.returnUrl3DS
            request.isTokenized = params.cardData.isTokenized
        } else if (params.paymentOption == PaymentOption.BankCard && params.cardData != null) {
            request.cardNumber = params.cardData.cardNumber
            request.cvv = params.cardData.cvv
            request.expiryYear = params.cardData.expiryDate!!.split("/")[1]
            request.expiryMonth = params.cardData.expiryDate.split("/")[0]
            request.cardHolder = params.cardData.cardHolder
            request.returnUrl3DS = params.cardData.returnUrl3DS
            request.isTokenized = params.cardData.isTokenized
        }
        if (params.paymentOption == PaymentOption.TokenizedCreditCard && params.tokenizationData != null) {
            request.cvv = params.tokenizationData.cvv
            request.returnUrl3DS = params.tokenizationData.returnUrl3DS
            request.tokenId = params.tokenizationData.tokenId
        }

        return paymentRepository.pay(request)

    }

}

data class PayUseCaseParams(

    val paymentOption: PaymentOption? = null,
    val merchantReferenceId: String? = null,
    val customerMerchantProfileId: String? = null,

    val customerFirstName: String? = null,
    val customerLastName: String? = null,
    val customerEmail: String? = null,
    val customerMobile: String? = null,

    val amount: Number? = null,
    val signature: String? = null,
    val description: String? = null,

    val isFeesOnCustomer: Boolean? = null,
    val cardData: CardData? = null,
    val tokenizationData: TokenizationData? = null


)