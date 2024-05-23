package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.web_view.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
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
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.web_view.view_models.WebViewViewModel
import com.luminsoft.cowpay_sdk.payment.data.models.pay_request.PayResponse
import com.luminsoft.cowpay_sdk.sdk.CowpaySDK
import com.luminsoft.cowpay_sdk.sdk.model.PaymentFailedModel
import com.luminsoft.cowpay_sdk.sdk.model.PaymentSuccessModel
import com.luminsoft.cowpay_sdk.ui.components.BottomSheet
import com.luminsoft.cowpay_sdk.ui.components.BottomSheetStatus
import org.json.JSONObject
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewBankCardContent(
    navController: NavController,
    payResponse: PayResponse,
    webViewViewModel: WebViewViewModel = koinViewModel()
) {
    val isFailed = webViewViewModel.isFailure.collectAsState()
    val responseModel = webViewViewModel.responseModel.collectAsState()
//    val showWebView = webViewViewModel.showWebView.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    if (isFailed.value == null && responseModel.value == null)
        AndroidView(factory = {
            val wv = WebView(it)
            wv.apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webChromeClient = WebChromeClient()
                webViewClient = WebViewClient()
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
//                        webViewViewModel.showWebView.value = false
                    }

                    override fun onReceivedError(
                        view: WebView,
                        request: WebResourceRequest,
                        error: WebResourceErrorCompat
                    ) {
                        super.onReceivedError(view, request, error)
//                        webViewViewModel.showWebView.value = false
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        Log.d("Webview", url ?: "")
                        val htmlStr = payResponse.html?.replace("\"", "\\\"")
                        val jsonObject = JSONObject("""{"html":"$htmlStr"}""")

                        val jsonString = jsonObject.toString()
                        Log.d("jsonString", jsonString)

                        view?.evaluateJavascript(
                            "setTimeout(()=>{ window.postMessage($jsonString); },3000)",
                            null
                        )
                    }


                }
                settings.javaScriptEnabled = true
                addJavascriptInterface(JSBankCardBridge({
                    webViewViewModel.isFailure.value = true
                    webViewViewModel.showWebView.value = false
                }, { it1 ->
                    webViewViewModel.responseModel.value = it1
                    webViewViewModel.showWebView.value = false
                }), "AndroidJsBridge")

            }
        }, update = {
            it.loadUrl(
                "${CowpaySDK.environment.getRedirectBaseUrl()}/customer-paymentlink/otp-redirect?token=${payResponse.returnUrl3DS}"
            )
        })
    else if (isFailed.value == true) {

        BottomSheet(
            bottomSheetStatus = BottomSheetStatus.ERROR,
            text = stringResource(id = R.string.paymentFailed),
            buttonText = stringResource(id = R.string.exit),
            onPressedButton = {
                activity.finish()
                CowpaySDK.cowpayCallback?.error(PaymentFailedModel(context.getString(R.string.paymentFailed)))
            },
        )
        {
            activity.finish()
            CowpaySDK.cowpayCallback?.error(PaymentFailedModel(context.getString(R.string.paymentFailed)))
        }


    } else if (responseModel.value != null) {

        BottomSheet(
            bottomSheetStatus = BottomSheetStatus.SUCCESS,
            text = "${stringResource(id = R.string.successPayment)}\n${stringResource(id = R.string.referenceNumber)}: ${responseModel.value?.orderNumber}",
            buttonText = stringResource(id = R.string.exit),
            onPressedButton = {
                activity.finish()
                CowpaySDK.cowpayCallback?.success(
                    PaymentSuccessModel.BankCardSuccessModel(
                        responseModel.value!!.orderNumber ?: ""
                    )
                )
            },
        )
        {
            activity.finish()
            CowpaySDK.cowpayCallback?.success(
                PaymentSuccessModel.BankCardSuccessModel(
                    responseModel.value!!.orderNumber ?: ""
                )
            )
        }


    }

}

class JSBankCardBridge(
    val onClickError: () -> Unit,
    val onClickSuccess: (CowpayJSChannelMessage) -> Unit
) {
    @JavascriptInterface
    fun showMessageInNative(message: String) {
        val obj = Gson().fromJson(message, CowpayJSChannelMessage::class.java)
        if (obj.paymentStatus == 2) {
            onClickSuccess(obj)
        } else {
            onClickError()
        }
    }
}