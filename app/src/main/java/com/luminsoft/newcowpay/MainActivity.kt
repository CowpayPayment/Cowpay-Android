package com.luminsoft.newcowpay

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.luminsoft.cowpay_sdk.sdk.CowpayEnvironment
import com.luminsoft.cowpay_sdk.sdk.CowpaySDK
import com.luminsoft.cowpay_sdk.sdk.LocalizationCode
import com.luminsoft.cowpay_sdk.sdk.model.CowpayCallback
import com.luminsoft.cowpay_sdk.sdk.model.PaymentFailedModel
import com.luminsoft.cowpay_sdk.sdk.model.PaymentInfo
import com.luminsoft.cowpay_sdk.sdk.model.PaymentSuccessModel
import com.luminsoft.cowpay_sdk.ui.components.NormalTextField
import com.luminsoft.newcowpay.ui.theme.NewcowpayTheme
import io.github.cdimascio.dotenv.dotenv
import java.util.Locale
import java.util.Random

val dotenv = dotenv {
    directory = "/assets"
    filename = "env"
}
var merchantMobileNumber = mutableStateOf(TextFieldValue(text = dotenv["MERCHANT_MOBILE"]))
var merchantHashKey = mutableStateOf(TextFieldValue(text = dotenv["HASH_KEY"]))
var merchantCode = mutableStateOf(TextFieldValue(text = dotenv["MERCHANT_CODE"]))
var isFeesOnCustomer = mutableStateOf(false)
var isArabic = mutableStateOf(false)
var userProfileId = mutableStateOf(TextFieldValue(text = "Example22"))
var merchantIconLink =
    mutableStateOf(TextFieldValue(text = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Amazon_logo.svg/2560px-Amazon_logo.svg.png"))

class MainActivity : ComponentActivity() {
    var text = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLocale("en")

        setContent {
            val activity = LocalContext.current as Activity
            NewcowpayTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp), verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(20.dp))
                        NormalTextField(
                            label = "Merchant Code",
                            value = merchantCode.value,
                            onValueChange = {
                                merchantCode.value = it
                            })

                        NormalTextField(
                            label = "Hash Key",
                            value = merchantHashKey.value,
                            onValueChange = {
                                merchantHashKey.value = it
                            })
                        NormalTextField(
                            label = "Merchant Mobile",
                            value = merchantMobileNumber.value,
                            onValueChange = {
                                merchantMobileNumber.value = it
                            })
                        NormalTextField(
                            label = "Logo Link",
                            value = merchantIconLink.value,
                            onValueChange = {
                                merchantIconLink.value = it
                            })
                        NormalTextField(
                            label = "user profile id",
                            value = userProfileId.value,
                            onValueChange = {
                                userProfileId.value = it
                            })

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Is Fees on Customer ?")
                            Switch(
                                modifier = Modifier.scale(0.8f),
                                checked = isFeesOnCustomer.value,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    uncheckedThumbColor = MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.5f
                                    ),
                                    uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.3f
                                    ),
                                    uncheckedBorderColor = Color.Unspecified,
                                    checkedBorderColor = Color.Unspecified,

                                    ),
                                onCheckedChange = {
                                    isFeesOnCustomer.value = it
                                },
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Is Arabic?")
                            Switch(
                                modifier = Modifier.scale(0.8f),
                                checked = isArabic.value,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    uncheckedThumbColor = MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.5f
                                    ),
                                    uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.3f
                                    ),
                                    uncheckedBorderColor = Color.Unspecified,
                                    checkedBorderColor = Color.Unspecified,

                                    ),
                                onCheckedChange = {
                                    isArabic.value = it
                                },
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text.value)
                    }

                    Column {
                        val border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        val modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                            .padding(
                                start = 15.dp, end = 15.dp
                            )
                        Button(
                            border = border,
                            modifier = modifier,
                            onClick = {
                                val paymentInfo = PaymentInfo(
                                    "1000${getRandomNumber()}",
                                    userProfileId.value.text,
                                    7.0,
                                    "Android",
                                    "SDK",
                                    "+201068890002",
                                    "mohamed.abdelmoniem@excelsystms-eg.com",
                                    "description",
                                    isFeesOnCustomer.value
                                )


                                try {
                                    CowpaySDK.init(
                                        merchantCode.value.text,
                                        merchantHashKey.value.text,
                                        merchantMobileNumber.value.text,
                                        paymentInfo,
                                        CowpayEnvironment.STAGING,
                                        localizationCode = isArabic.value.let { if (it) LocalizationCode.AR else LocalizationCode.EN },
                                        merchantLogo = merchantIconLink.value.text
                                    )
                                } catch (e: Exception) {
                                    Log.e("error", e.toString())
                                }
                                try {
                                    CowpaySDK.launch(activity, object : CowpayCallback {
                                        override fun success(paymentSuccessModel: PaymentSuccessModel) {
//                                        if (paymentSuccessModel is PaymentSuccessModel.FawrySuccessModel) {
//                                            Log.e("SuccessFawry", paymentSuccessModel.paymentMethodName)
//                                        } else if (paymentSuccessModel is PaymentSuccessModel.CreditCardSuccessModel) {
//                                            Log.e("SuccessCard", paymentSuccessModel.paymentReferenceId)
//                                            Log.e("SuccessCard", paymentSuccessModel.paymentMethodName)
//                                        }
//                                        Log.e("SuccessCard", paymentSuccessModel.toString())
                                            text.value =
                                                "payment method: ${paymentSuccessModel.paymentMethodName} \nReference number: ${paymentSuccessModel.paymentReferenceId}"
                                        }

                                        override fun error(paymentFailedModel: PaymentFailedModel) {
                                            text.value = paymentFailedModel.failureMessage

                                        }

                                        override fun closedByUser() {
                                            text.value = "Closed by user"
                                        }

                                    })
                                } catch (e: Exception) {
                                    Log.e("error", e.toString())
                                }
                            },
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),

                            ) {
                            Text(
                                text = "Launch CowPay",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }


    }

    fun setLocale(lang: String?) {
        val locale = lang?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        val config: Configuration = getBaseContext().getResources().getConfiguration()
        config.setLocale(locale)
        getBaseContext().getResources().updateConfiguration(
            config,
            getBaseContext().getResources().getDisplayMetrics()
        )
    }

    private fun getRandomNumber(): String {
        val r = Random()
        return "%04d".format(r.nextInt(1001))
    }
}
