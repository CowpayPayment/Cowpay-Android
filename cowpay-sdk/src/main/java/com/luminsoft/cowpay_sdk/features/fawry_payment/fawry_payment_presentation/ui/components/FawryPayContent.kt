package com.luminsoft.cowpay_sdk.features.fawry_payment.fawry_payment_presentation.ui.components

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.cowpay_sdk.R
import com.luminsoft.cowpay_sdk.failures.AuthFailure
import com.luminsoft.cowpay_sdk.failures.ServerFailure
import com.luminsoft.cowpay_sdk.features.fawry_payment.fawry_payment_presentation.view_model.FawryViewModel
import com.luminsoft.cowpay_sdk.form_fields.MobileNumber
import com.luminsoft.cowpay_sdk.payment.presentation.ui.PaymentTotalDetails
import com.luminsoft.cowpay_sdk.sdk.CowpaySDK
import com.luminsoft.cowpay_sdk.sdk.model.PaymentFailedModel
import com.luminsoft.cowpay_sdk.sdk.model.PaymentSuccessModel
import com.luminsoft.cowpay_sdk.ui.components.BackGroundView
import com.luminsoft.cowpay_sdk.ui.components.BottomSheet
import com.luminsoft.cowpay_sdk.ui.components.BottomSheetStatus
import com.luminsoft.cowpay_sdk.ui.components.ButtonView
import com.luminsoft.cowpay_sdk.ui.components.LoadingView
import com.luminsoft.cowpay_sdk.ui.components.SpinKitLoadingIndicator
import com.simon.xmaterialccp.data.ccpDefaultColors
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.component.isPhoneNumber
import org.koin.androidx.compose.koinViewModel

@Composable
fun FawryPayContent(
    fawryViewModel: FawryViewModel = koinViewModel(),
    navController: NavController
) {
    val loading = fawryViewModel.loading.collectAsState()
    val failure = fawryViewModel.failure.collectAsState()
    val feesResponse = fawryViewModel.feesResponse.collectAsState()
    val payResponse = fawryViewModel.payResponse.collectAsState()
    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    if (loading.value) {
        LoadingView()
    } else if (!loading.value && feesResponse.value != null) {
        BackGroundView(
            title = stringResource(id = R.string.payment_details),
            navController = navController,
            isBackButton = true
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        stringResource(id = R.string.enter_your_mobile_number),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    CountryCodePickerFunction(fawryViewModel, context)
//                    InternationalMobilePhoneTogi(
//                        fawryViewModel, context,
//                    )
//                    Spacer(modifier = Modifier.height(5.dp))
//                    MobileNumberTextField()

                    Spacer(modifier = Modifier.height(20.dp))
                    feesResponse.value?.let {
                        CowpaySDK.paymentInfo?.amount?.let { it1 ->
                            PaymentTotalDetails(
                                it,
                                it1, CowpaySDK.paymentInfo!!.isFeesOnCustomer
                            )
                        }
                    }

                }

                Column {
                    ConfirmButton(
                        onClick = { fawryViewModel.pay() },
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                if (payResponse.value != null) {
                    FawryBottomSheet(payResponse.value?.paymentGatewayReferenceId ?: "", {
                        activity.finish()
                        CowpaySDK.cowpayCallback?.success(
                            PaymentSuccessModel.FawrySuccessModel(
                                payResponse.value?.cowpayReferenceId ?: "",
                                payResponse.value?.paymentGatewayReferenceId ?: ""
                            )
                        )
                    }, {
                        activity.finish()
                        CowpaySDK.cowpayCallback?.success(
                            PaymentSuccessModel.FawrySuccessModel(
                                payResponse.value?.cowpayReferenceId ?: "",
                                payResponse.value?.paymentGatewayReferenceId ?: ""
                            )
                        )
                    })
                }
            }
            if (!failure.value?.message.isNullOrEmpty()) {
                if (failure.value is ServerFailure && feesResponse.value != null) {
                    failure.value?.let {
                        BottomSheet(
                            bottomSheetStatus = BottomSheetStatus.ERROR,
                            text = it.message,
                            buttonText = stringResource(id = R.string.exit),
                            onPressedButton = {
                                activity.finish()
                                CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message, it))
                            },
                        )
                        {
                            activity.finish()
                            CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message, it))
                        }
                    }
                } else {
                    failure.value?.let {
                        BottomSheet(
                            bottomSheetStatus = BottomSheetStatus.ERROR,
                            text = it.message,
                            buttonText = stringResource(id = R.string.retry),
                            onPressedButton = {
                                fawryViewModel.retry()
                            },
                            secondButtonText = stringResource(id = R.string.exit),
                            onPressedSecondButton = {
                                activity.finish()
                                CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message, it))
                            }
                        )
                        {
                            activity.finish()
                            CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message, it))
                        }
                    }

                }
            }
        }

    } else if (!failure.value?.message.isNullOrEmpty()) {
        if (failure.value is AuthFailure) {
            failure.value?.let {
                BottomSheet(
                    bottomSheetStatus = BottomSheetStatus.ERROR,
                    text = it.message,
                    buttonText = stringResource(id = R.string.exit),
                    onPressedButton = {
                        activity.finish()
                        CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message, it))
                    },
                )
                {
                    activity.finish()
                    CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message, it))
                }
            }
        } else if (feesResponse.value == null) {
            failure.value?.let {
                BottomSheet(
                    bottomSheetStatus = BottomSheetStatus.ERROR,
                    text = it.message,
                    buttonText = stringResource(id = R.string.retry),
                    onPressedButton = {
                        fawryViewModel.retry()
                    },
                    secondButtonText = stringResource(id = R.string.exit),
                    onPressedSecondButton = {
                        activity.finish()
                        CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message, it))
                    }
                )
                {
                    activity.finish()
                    CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message, it))
                }
            }

        }
    }
}


@Composable
fun ConfirmButton(
    fawryViewModel: FawryViewModel = koinViewModel(),
    onClick: () -> Unit
) {
    val isValid: Boolean =
        fawryViewModel.mobileNumber.collectAsState().value?.value?.isRight() ?: false

    val isButtonLoading = fawryViewModel.isButtonLoading.collectAsState()

    if (!isButtonLoading.value) {
        ButtonView(
            onClick = {
                if (!isPhoneNumber()) {
                    onClick()
                }
            },
            stringResource(id = R.string.proceed),
            isEnabled = (!isPhoneNumber())
        )
    } else {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            SpinKitLoadingIndicator()
        }

    }
}

@Composable
fun CountryCodePickerFunction(
    fawryViewModel: FawryViewModel,
    context: Context,
) {
    var enteredText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            TogiCountryCodePicker(
                text = enteredText,
                onValueChange = {
                    enteredText = it
                     fawryViewModel.mobileNumber.value =
                         MobileNumber(it)
                },
            )
        }

    }
}
