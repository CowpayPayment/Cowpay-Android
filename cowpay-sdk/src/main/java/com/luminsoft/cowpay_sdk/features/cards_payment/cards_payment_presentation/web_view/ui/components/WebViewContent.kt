package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.web_view.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.webkit.WebResourceErrorCompat
import androidx.webkit.WebViewClientCompat
import com.google.gson.Gson
import com.luminsoft.cowpay_sdk.R
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.CowpayJSChannelMessage
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.CowpayJSChannelMessageStatusEnum
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.web_view.view_models.WebViewViewModel
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayResponse
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.RedirectStatusEnum
import com.luminsoft.cowpay_sdk.sdk.CowpaySDK
import com.luminsoft.cowpay_sdk.sdk.model.PaymentFailedModel
import com.luminsoft.cowpay_sdk.sdk.model.PaymentSuccessModel
import com.luminsoft.cowpay_sdk.ui.components.BottomSheet
import com.luminsoft.cowpay_sdk.ui.components.BottomSheetStatus
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewContent(
    navController: NavController,
    payResponse: PayResponse,
    webViewViewModel: WebViewViewModel = koinViewModel<WebViewViewModel>()
) {
    val isFailed = webViewViewModel.isFailure.collectAsState()
    val exitWarning = webViewViewModel.exitWarning.collectAsState()
    val responseModel = webViewViewModel.responseModel.collectAsState()
    val showWebView = webViewViewModel.showWebView.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    var postData = ""
    if (payResponse.getRedirectStatus() == RedirectStatusEnum.Redirect) {
        postData = "body=${
            URLEncoder.encode(
                payResponse.redirectParams?.body,
                "UTF-8"
            )
        }"
    } else if (payResponse.getRedirectStatus() == RedirectStatusEnum.ThreeDS) {
        "paReq=${
            URLEncoder.encode(
                payResponse.redirectParams?.paReq,
                "UTF-8"
            )
        },termURL=${
            URLEncoder.encode(
                payResponse.redirectParams?.termUrl,
                "UTF-8"
            )
        },md=${
            URLEncoder.encode(
                payResponse.redirectParams?.md,
                "UTF-8"
            )
        }"
    }
    if(isFailed.value== null && responseModel.value == null && showWebView.value ) {
        BackHandler(
            enabled = true, onBack = {
                webViewViewModel.exitWarning.value = true
            })
        AndroidView(factory = {
            val wv = WebView(it)
            wv.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClientCompat() {
                    override fun onReceivedHttpError(
                        view: WebView,
                        request: WebResourceRequest,
                        errorResponse: WebResourceResponse
                    ) {
                        super.onReceivedHttpError(view, request, errorResponse)
                    }

                    override fun onReceivedError(
                        view: WebView,
                        request: WebResourceRequest,
                        error: WebResourceErrorCompat
                    ) {
                        super.onReceivedError(view, request, error)
                        webViewViewModel.showWebView.value = false
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d("Webview", url ?: "")
                    }

                }
                settings.javaScriptEnabled = true
                addJavascriptInterface(JSBridge({
                    webViewViewModel.isFailure.value = true
                    webViewViewModel.showWebView.value = false
                }, { it1 ->
                    webViewViewModel.responseModel.value = it1
                    webViewViewModel.showWebView.value = false
                }), "AndroidJsBridge")
                postUrl(payResponse.redirectUrl ?: "", postData.toByteArray())
            }
        }, update = {
            it.postUrl(payResponse.redirectUrl ?: "", postData.toByteArray())
        })
         if (exitWarning.value) {
            BottomSheet(
                bottomSheetStatus = BottomSheetStatus.WARNING,
                text = stringResource(id = R.string.youAreAboutToExitPayment),
                buttonText =  stringResource(id = R.string.exit),
                onPressedButton = {
                    activity.finish()
                    CowpaySDK.cowpayCallback?.closedByUser()
                },
                secondButtonText = stringResource(id = R.string.dismiss),
                onPressedSecondButton = {
                    webViewViewModel.exitWarning.value = false
                }
            )
            {
                webViewViewModel.exitWarning.value = false
            }


        }
    }
    else if (isFailed.value== true) {

        BottomSheet(
            bottomSheetStatus = BottomSheetStatus.ERROR,
            text = stringResource(id = R.string.paymentFailed),
            buttonText =  stringResource(id = R.string.exit),
            onPressedButton = {
                activity.finish()
                CowpaySDK.cowpayCallback?.error(PaymentFailedModel(context.getString( R.string.paymentFailed)))
            },
        )
        {
            activity.finish()
            CowpaySDK.cowpayCallback?.error(PaymentFailedModel(context.getString( R.string.paymentFailed)))
        }


    }else if (responseModel.value != null) {

        BottomSheet(
            bottomSheetStatus = BottomSheetStatus.SUCCESS,
            text = "${stringResource(id = R.string.successPayment)}\n${stringResource(id = R.string.referenceNumber)}: ${responseModel.value?.orderId}",
            buttonText =  stringResource(id = R.string.exit),
            onPressedButton = {
                activity.finish()
                CowpaySDK.cowpayCallback?.success(PaymentSuccessModel.CreditCardSuccessModel(payResponse.cowpayReferenceId?:""))
            },
        )
        {
            activity.finish()
            CowpaySDK.cowpayCallback?.success(PaymentSuccessModel.CreditCardSuccessModel(responseModel.value!!.orderId?:""))
        }


    }

}

class JSBridge(val onClickError: () -> Unit, val onClickSuccess: (CowpayJSChannelMessage) -> Unit) {
    @JavascriptInterface
    fun showMessageInNative(message: String) {
        val obj = Gson().fromJson(message, CowpayJSChannelMessage::class.java)
        if (obj.parseCowpayJSChannelMessageStatusEnum() == CowpayJSChannelMessageStatusEnum.Success) {
            onClickSuccess(obj)
        } else {
            onClickError()
        }
    }
}