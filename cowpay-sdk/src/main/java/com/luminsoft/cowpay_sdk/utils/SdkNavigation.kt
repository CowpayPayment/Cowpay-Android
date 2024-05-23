package com.luminsoft.cowpay_sdk.utils

sealed class SdkNavigation(val route: String) {
    object PaymentMethodsScreen : SdkNavigation(route = "PaymentMethodsScreen")
    object AddCardScreen : SdkNavigation(route = "AddCardScreen")
    object SavedCardScreen : SdkNavigation(route = "SavedCardScreen")
    object AddBankCardScreen : SdkNavigation(route = "AddBankCardScreen")
    object WebViewScreen : SdkNavigation(route = "WebViewScreen")
    object WebViewBankCardScreen : SdkNavigation(route = "WebViewBankCardScreen")
    object FawryPayScreen : SdkNavigation(route = "FawryPayScreen")
}