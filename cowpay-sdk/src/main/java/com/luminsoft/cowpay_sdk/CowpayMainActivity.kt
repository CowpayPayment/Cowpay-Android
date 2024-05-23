package com.luminsoft.cowpay_sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.add_card.ui.components.AddBankCardContent
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.add_card.ui.components.AddCardContent
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.saved_cards.ui.components.SavedCardsContent
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.web_view.ui.components.WebViewBankCardContent
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.web_view.ui.components.WebViewContent
import com.luminsoft.cowpay_sdk.features.cards_payment.di.cardsPaymentModule
import com.luminsoft.cowpay_sdk.features.fawry_payment.di.fawryModule
import com.luminsoft.cowpay_sdk.features.fawry_payment.fawry_payment_presentation.ui.components.FawryPayContent
import com.luminsoft.cowpay_sdk.features.payment_methods.di.paymentMethodsModule
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_presentation.ui.components.PaymentMethodScreenContent
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayResponse
import com.luminsoft.cowpay_sdk.payment.di.paymentModule
import com.luminsoft.cowpay_sdk.sdk.appModule
import com.luminsoft.cowpay_sdk.ui.theme.CowpayappTheme
import com.luminsoft.cowpay_sdk.utils.ResourceProvider
import com.luminsoft.cowpay_sdk.utils.SdkNavigation
import com.luminsoft.cowpay_sdk.utils.WifiService
import org.koin.core.Koin
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin


class CowpayMainActivity : ComponentActivity() {
    private fun setupServices() {

        WifiService.instance.initializeWithApplicationContext(this)
        ResourceProvider.instance.initializeWithApplicationContext(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getKoin(this)

        setupServices()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            CowpayappTheme (dynamicColor = false){
                NavHost(
                    navController = navController,
                    startDestination = SdkNavigation.PaymentMethodsScreen.route
                ) {
                    composable(SdkNavigation.PaymentMethodsScreen.route) {
                        PaymentMethodScreenContent(navController = navController)
                    }

                    composable(SdkNavigation.SavedCardScreen.route) {
                        SavedCardsContent(
                            navController = navController
                        )
                    }
                    composable(SdkNavigation.AddBankCardScreen.route) {
                        AddBankCardContent(
                            navController = navController
                        )
                    }
                    composable(
                        SdkNavigation.AddCardScreen.route + "/{isSavedCards}",
                        arguments = listOf(navArgument("isSavedCards") {
                            defaultValue = true
                            type = NavType.BoolType
                        })
                    ) {
                        AddCardContent(
                            navController = navController,
                            isSavedCards = it.arguments?.getBoolean("isSavedCards") ?: true
                        )
                    }
                    composable(SdkNavigation.FawryPayScreen.route) {
                        FawryPayContent(navController = navController)
                    }
                    composable(SdkNavigation.WebViewScreen.route) {
                        val payResponse: PayResponse? =
                            navController.previousBackStackEntry?.savedStateHandle?.get("payResponse") as PayResponse?
                        if (payResponse != null)
                            WebViewContent(navController = navController, payResponse = payResponse)
                        else {
                            throw Exception()
                        }
                    }
                    composable(SdkNavigation.WebViewBankCardScreen.route) {
                        val payResponse: PayResponse? =
                            navController.previousBackStackEntry?.savedStateHandle?.get("payResponse") as PayResponse?
                        if (payResponse != null)
                            WebViewBankCardContent(navController = navController, payResponse = payResponse)
                        else {
                            throw Exception()
                        }
                    }

//                    paymentMethodsScreen(navController)
//                    savedCardScreen(navController)
//                    addCardScreen(navController)
//                    fawryPayScreen(navController)
//                    webViewScreen(navController)
                }
            }
        }
    }

    private fun getKoin(activity: ComponentActivity): Koin {
        return if (activity is KoinComponent) {
            activity.getKoin()
        } else {
            GlobalContext.getOrNull() ?: startKoin {
                modules(appModule)
                modules(cardsPaymentModule)
                modules(paymentModule)
                modules(paymentMethodsModule)
                modules(fawryModule)

            }.koin
        }

    }
}