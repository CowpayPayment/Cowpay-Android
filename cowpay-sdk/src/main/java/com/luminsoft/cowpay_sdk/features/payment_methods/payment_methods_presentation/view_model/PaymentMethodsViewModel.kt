package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_presentation.view_model

import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.usecases.GetPaymentMethodsUseCase
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.usecases.GetPaymentMethodsUseCaseParams
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.usecases.GetTokenUseCase
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.usecases.GetTokenUseCaseParams
import com.luminsoft.cowpay_sdk.network.RetroClient
import com.luminsoft.cowpay_sdk.payment.data.models.PaymentOption
import com.luminsoft.cowpay_sdk.sdk.CowpaySDK
import com.luminsoft.cowpay_sdk.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow

class PaymentMethodsViewModel(
    private val getTokenUseCase: GetTokenUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase
) : ViewModel() {

    var selectedPaymentOption: MutableStateFlow<PaymentOption?> = MutableStateFlow(null)
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var methodsResponse: MutableStateFlow<ArrayList<PaymentOption>?> = MutableStateFlow(null)

    init {
        getToken()
    }

    private fun getToken() {
        loading.value = true
        ui {
            params.value =
                GetTokenUseCaseParams(CowpaySDK.merchantCode, CowpaySDK.merchantPhoneNumber)

            val response: Either<SdkFailure, String> =
                getTokenUseCase.call(params.value as GetTokenUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        RetroClient.token = it1
                        getMethods()
                    }
                })
        }


    }

    private fun getMethods() {
        ui {
            params.value = GetPaymentMethodsUseCaseParams(CowpaySDK.merchantCode)
            val response: Either<SdkFailure, ArrayList<PaymentOption>?> =
                getPaymentMethodsUseCase.call(params.value as GetPaymentMethodsUseCaseParams)
            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                {
                    it?.let {
                        methodsResponse.value = it
                        loading.value = false
                    }
                })
        }
    }

    fun retry() {
        failure.value = null
        if (params.value is GetPaymentMethodsUseCaseParams) {
            getMethods()
        } else if (params.value is GetTokenUseCaseParams) {
            getToken()
        }
    }
   fun changeMethodOption(paymentOption: PaymentOption){
       selectedPaymentOption.value = paymentOption
    }
}