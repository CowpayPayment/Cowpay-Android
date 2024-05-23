package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.saved_cards.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import arrow.core.Either
import com.luminsoft.cowpay_sdk.failures.SdkFailure
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.TokenizedCardData
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_domain.usecases.GetSavedCardsUseCase
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_domain.usecases.GetSavedCardsUseCaseParams
import com.luminsoft.cowpay_sdk.form_fields.CVV
import com.luminsoft.cowpay_sdk.payment.data.models.PaymentOption
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesResponse
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayResponse
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.TokenizationData
import com.luminsoft.cowpay_sdk.payment.domain.usecases.GenerateSignatureUseCase
import com.luminsoft.cowpay_sdk.payment.domain.usecases.GenerateSignatureUseCaseParams
import com.luminsoft.cowpay_sdk.payment.domain.usecases.GetFeesUseCase
import com.luminsoft.cowpay_sdk.payment.domain.usecases.GetFeesUseCaseParams
import com.luminsoft.cowpay_sdk.payment.domain.usecases.PayUseCase
import com.luminsoft.cowpay_sdk.payment.domain.usecases.PayUseCaseParams
import com.luminsoft.cowpay_sdk.sdk.CowpaySDK
import com.luminsoft.cowpay_sdk.utils.SdkNavigation
import com.luminsoft.cowpay_sdk.utils.ui
import kotlinx.coroutines.flow.MutableStateFlow

class SavedCardsViewModel(
    private val payUseCase: PayUseCase,
    private val getFees: GetFeesUseCase,
    private val getSavedCardsUseCase: GetSavedCardsUseCase
) : ViewModel() {

    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isCvvBottomSheetOpen: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isButtonLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var failure: MutableStateFlow<SdkFailure?> = MutableStateFlow(null)
    private var params: MutableStateFlow<Any?> = MutableStateFlow(null)
    var feesResponse:MutableStateFlow<GetFeesResponse?> = MutableStateFlow(null)

    var savedCards = MutableStateFlow<ArrayList<TokenizedCardData>?>(null)
    var cvvTextValue = mutableStateOf(TextFieldValue())
    var cvv = MutableStateFlow<CVV?>(null)

    private var payResponse = MutableStateFlow<PayResponse?>(null)
    var selectedCard = MutableStateFlow<TokenizedCardData?>(null)


    lateinit var navController: NavController

    init {
        getSavedCards()
    }

    private fun getFees() {
        loading.value = true
        ui {
            params.value =
                CowpaySDK.paymentInfo?.amount?.let {
                    GetFeesUseCaseParams(
                        CowpaySDK.merchantCode,
                        it, PaymentOption.CreditCard.getId()
                    )
                }

            val response: Either<SdkFailure, GetFeesResponse> =
                getFees.call(params.value as GetFeesUseCaseParams)

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

    private fun getSavedCards() {
        loading.value = true
        ui {
            params.value =
                CowpaySDK.paymentInfo?.let {
                    GetSavedCardsUseCaseParams(
                        CowpaySDK.merchantCode,
                        it.customerMerchantProfileId
                    )
                }

            val response: Either<SdkFailure, ArrayList<TokenizedCardData>?> =
                getSavedCardsUseCase.call(params.value as GetSavedCardsUseCaseParams)

            response.fold(
                {
                    failure.value = it
                    loading.value = false
                },
                { s ->
                    s.let { it1 ->
                        savedCards.value = it1
                        if(savedCards.value.isNullOrEmpty()){
                            val isSavedCards = false
                            navController.navigate("${SdkNavigation.AddCardScreen.route}/{isSavedCards}".replace(oldValue = "{isSavedCards}", newValue = "$isSavedCards"))
                        }else{
                            getFees()
                        }

                    }
                })
        }
    }

    fun pay() {
        isButtonLoading.value = true
        isCvvBottomSheetOpen.value = false
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
                PaymentOption.TokenizedCreditCard,
                CowpaySDK.paymentInfo?.merchantReferenceId,
                CowpaySDK.paymentInfo?.customerMerchantProfileId,
                CowpaySDK.paymentInfo?.customerFirstName,
                CowpaySDK.paymentInfo?.customerLastName,
                CowpaySDK.paymentInfo?.customerEmail,
                CowpaySDK.paymentInfo?.customerMobile,
                CowpaySDK.paymentInfo?.amount,
                signature,
                CowpaySDK.paymentInfo?.description,
                CowpaySDK.paymentInfo?.isFeesOnCustomer,
                tokenizationData = TokenizationData(
                    returnUrl3DS = "${CowpaySDK.getBaseUrl()}:8070/customer-paymentlink/otp-redirect",
                    tokenId = selectedCard.value?.tokenId,
                    cvv = cvv.value?.value?.fold({ null }, { it }),
                )
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
                        navController.currentBackStackEntry?.savedStateHandle?.set("payResponse", it)
                        navController.navigate(SdkNavigation.WebViewScreen.route)
                        isButtonLoading.value = false
                    }
                })
        }
    }
    fun retry() {
        failure.value = null
        when (params.value) {
            is GetSavedCardsUseCaseParams -> {
                getSavedCards()
            }

            is GetFeesUseCaseParams -> {
                getFees()
            }

            is PayUseCaseParams -> {
                pay()
            }
        }
    }

    fun changeSelectedCard(tokenizedCardData: TokenizedCardData) {
        selectedCard.value = tokenizedCardData
        cvv.value = null
        cvvTextValue.value = TextFieldValue()
    }
}