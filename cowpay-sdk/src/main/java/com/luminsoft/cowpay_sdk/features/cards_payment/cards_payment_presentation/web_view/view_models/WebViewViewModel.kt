package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.web_view.view_models

import androidx.lifecycle.ViewModel
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.CowpayJSChannelMessage
import kotlinx.coroutines.flow.MutableStateFlow

class WebViewViewModel : ViewModel() {
    var loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var showWebView: MutableStateFlow<Boolean> = MutableStateFlow(true)
    var isFailure: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    var exitWarning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var responseModel: MutableStateFlow<CowpayJSChannelMessage?> = MutableStateFlow(null)
}