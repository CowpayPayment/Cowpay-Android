package com.luminsoft.cowpay_sdk.features.fawry_payment.fawry_payment_presentation.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.form_fields.MobileNumber
import com.luminsoft.cowpay_sdk.payment.data.models.PaymentOption
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesResponse
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayResponse
import com.luminsoft.cowpay_sdk.payment.domain.usecases.GenerateSignatureUseCase
import com.luminsoft.cowpay_sdk.payment.domain.usecases.GenerateSignatureUseCaseParams
import com.luminsoft.cowpay_sdk.payment.domain.usecases.GetFeesUseCase
import com.luminsoft.cowpay_sdk.payment.domain.usecases.GetFeesUseCaseParams
import com.luminsoft.cowpay_sdk.payment.domain.usecases.PayUseCase
import com.luminsoft.cowpay_sdk.payment.domain.usecases.PayUseCaseParams
import com.luminsoft.cowpay_sdk.sdk.CowpaySDK
import com.luminsoft.cowpay_sdk.utils.ui
import com.togitech.ccp.component.getFullPhoneNumber
import kotlinx.coroutines.flow.MutableStateFlow

class FawryViewModel(
    private val payUseCase: PayUseCase,
    private val getFeesUseCase: GetFeesUseCase
) : ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)


    var mobileNumber = MutableStateFlow<MobileNumber?>(MobileNumber(""))

    var feesResponse = MutableStateFlow<GetFeesResponse?>(null)
    var payResponse = MutableStateFlow<PayResponse?>(null)

    init {
        getFees()
    }

    private fun getFees() {
        loading.value = true
        ui {
            params.value =
                CowpaySDK.paymentInfo?.amount?.let {
                    GetFeesUseCaseParams(
                        CowpaySDK.merchantCode,
                        it, PaymentOption.FawryPay.getId()
                    )
                }

            val response: Either<SdkFailure, GetFeesResponse> =
                getFeesUseCase.call(params.value as GetFeesUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        feesResponse.value = it1
                        loading.value = false
                    }
                })
        }


    }

    fun pay() {
        isButtonLoading.value = true
        ui {
            val generateSignatureUseCase = GenerateSignatureUseCase()
            val signature = generateSignatureUseCase.call(
                GenerateSignatureUseCaseParams(
                    CowpaySDK.merchantCode,
                    CowpaySDK.paymentInfo!!.merchantReferenceId,
                    CowpaySDK.paymentInfo!!.customerMerchantProfileId,
                    CowpaySDK.paymentInfo!!.amount,
                    CowpaySDK.hashKey
                )
            )

            params.value = PayUseCaseParams(
                PaymentOption.FawryPay,
                CowpaySDK.paymentInfo?.merchantReferenceId,
                CowpaySDK.paymentInfo?.customerMerchantProfileId,
                CowpaySDK.paymentInfo?.customerFirstName,
                CowpaySDK.paymentInfo?.customerLastName,
                CowpaySDK.paymentInfo?.customerEmail,
                /*mobileNumber.value?.value?.fold({ null }, { it })*/getFullPhoneNumber(),
                CowpaySDK.paymentInfo?.amount,
                signature,
                CowpaySDK.paymentInfo?.description,
                CowpaySDK.paymentInfo?.isFeesOnCustomer,
            )

            val response: Either<SdkFailure, PayResponse> =
                payUseCase.call(params.value as PayUseCaseParams)
            response.fold(
                {
                    failure.value = it
                    isButtonLoading.value = false
                },
                {
                    it.let {
                        payResponse.value = it
                        isButtonLoading.value = false
                    }
                })
        }
    }

    fun retry() {
        failure.value = null
        if (params.value is GetFeesUseCaseParams) {
            getFees()
        } else if (params.value is PayUseCaseParams) {
            pay()
        }
    }
}