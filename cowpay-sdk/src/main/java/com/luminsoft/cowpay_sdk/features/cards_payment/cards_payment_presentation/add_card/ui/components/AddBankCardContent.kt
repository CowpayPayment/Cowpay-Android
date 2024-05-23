package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.add_card.ui.components

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.cowpay_sdk.R
import com.luminsoft.cowpay_sdk.failures.AuthFailure
import com.luminsoft.cowpay_sdk.failures.ServerFailure
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.add_card.view_model.AddBankCardViewModel
import com.luminsoft.cowpay_sdk.form_fields.CVV
import com.luminsoft.cowpay_sdk.form_fields.CardExpiryDate
import com.luminsoft.cowpay_sdk.form_fields.CardHolderName
import com.luminsoft.cowpay_sdk.form_fields.CardNumber
import com.luminsoft.cowpay_sdk.payment.presentation.ui.PaymentTotalDetails
import com.luminsoft.cowpay_sdk.sdk.CowpaySDK
import com.luminsoft.cowpay_sdk.sdk.model.PaymentFailedModel
import com.luminsoft.cowpay_sdk.sdk.model.PaymentSuccessModel
import com.luminsoft.cowpay_sdk.ui.components.BackGroundView
import com.luminsoft.cowpay_sdk.ui.components.BottomSheet
import com.luminsoft.cowpay_sdk.ui.components.BottomSheetStatus
import com.luminsoft.cowpay_sdk.ui.components.ButtonView
import com.luminsoft.cowpay_sdk.ui.components.LoadingView
import com.luminsoft.cowpay_sdk.ui.components.MaskVisualTransformation
import com.luminsoft.cowpay_sdk.ui.components.NormalTextField
import com.luminsoft.cowpay_sdk.ui.components.SpinKitLoadingIndicator
import com.luminsoft.cowpay_sdk.utils.SdkNavigation
import org.koin.androidx.compose.koinViewModel


@Composable
fun AddBankCardContent(
    addCardViewModel: AddBankCardViewModel = koinViewModel(),
    navController: NavController,
    isSavedCards: Boolean = true
) {
    val loading = addCardViewModel.loading.collectAsState()
    val failure = addCardViewModel.failure.collectAsState()
    val feesResponse = addCardViewModel.feesResponse.collectAsState()
    val payResponse = addCardViewModel.payResponse.collectAsState()
//    val isTokenization = addCardViewModel.isTokenization.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    if (loading.value) {
        LoadingView()
    } else if (!loading.value && feesResponse.value != null) {
        BackGroundView(
            title = stringResource(id = R.string.add_payment_card),
            navController = navController,
            isBackButton = true,
            onClick = isSavedCards.let {
                if (it) {
                    null
                } else {
                    {
                        navController.popBackStack(
                            route = SdkNavigation.PaymentMethodsScreen.route,
                            inclusive = false
                        )
                    }
                }
            }) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        stringResource(id = R.string.add_payment_card),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    CardBankHolderTextField()
                    CardBankNumberTextField()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                                .height(80.dp)
                        ) {
                            CardBankExpiryDateTextField()
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f)
                                .height(80.dp)
                        ) {
                            CardBankCvvTextField()
                        }

                    }
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Box(modifier = Modifier.weight(0.85f)) {
//                            Text(
//                                stringResource(id = R.string.save_this_card),
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onSurface
//                            )
//                        }
//                        Switch(
//                            modifier = Modifier.scale(0.8f),
//                            checked = isTokenization.value,
//                            colors = SwitchDefaults.colors(
//                                checkedThumbColor = MaterialTheme.colorScheme.primary,
//                                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
//                                uncheckedThumbColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
//                                uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
//                                uncheckedBorderColor = Color.Unspecified,
//                                checkedBorderColor = Color.Unspecified,
//
//                                ),
//                            onCheckedChange = {
//                                addCardViewModel.isTokenization.value = it
//                            },
//                        )
//                    }
                    Spacer(modifier = Modifier.height(10.dp))
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
                    ConfirmCardBankButton(onClick = { addCardViewModel.pay(navController) })
                    Spacer(modifier = Modifier.height(20.dp))
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
                                addCardViewModel.retry(navController)
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
            if (!payResponse.value?.statusId.isNullOrEmpty()) {
                if (payResponse.value?.statusId == "2") {
                    BottomSheet(
                        bottomSheetStatus = BottomSheetStatus.SUCCESS,
                        text = "${stringResource(id = R.string.successPayment)}\n${stringResource(id = R.string.referenceNumber)}: ${payResponse.value?.cowpayReferenceId}",
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            CowpaySDK.cowpayCallback?.success(
                                PaymentSuccessModel.CreditCardSuccessModel(
                                    payResponse.value!!.paymentGatewayReferenceId ?: ""
                                )
                            )
                        },
                    )
                    {
                        activity.finish()
                        CowpaySDK.cowpayCallback?.success(
                            PaymentSuccessModel.CreditCardSuccessModel(
                                payResponse.value!!.paymentGatewayReferenceId ?: ""
                            )
                        )
                    }

                } else if (failure.value != null) {
                    failure.value?.let {
                        BottomSheet(
                            bottomSheetStatus = BottomSheetStatus.ERROR,
                            text = it.message,
                            buttonText = stringResource(id = R.string.retry),
                            onPressedButton = {
                                addCardViewModel.retry(navController)
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
                } else if (payResponse.value?.statusId != "8" && payResponse.value?.statusId != "9") {
                    BottomSheet(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = context.getString(R.string.paymentFailed),
                        buttonText = stringResource(id = R.string.exit),
                        onPressedButton = {
                            activity.finish()
                            CowpaySDK.cowpayCallback?.error(
                                PaymentFailedModel(
                                    context.getString(R.string.paymentFailed),
                                    null
                                )
                            )
                        },

                        )
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
                        addCardViewModel.retry(navController)
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
fun ConfirmCardBankButton(
    addCardViewModel: AddBankCardViewModel = koinViewModel(),
    onClick: () -> Unit
) {
    val isValid: Boolean =
        (addCardViewModel.expiryDate.collectAsState().value?.value?.isRight() ?: false
                && addCardViewModel.cvv.collectAsState().value?.value?.isRight() ?: false
                && addCardViewModel.cardNumber.collectAsState().value?.value?.isRight() ?: false
                && addCardViewModel.holderName.collectAsState().value?.value?.isRight() ?: false)

    val isButtonLoading = addCardViewModel.isButtonLoading.collectAsState()

    if (!isButtonLoading.value) {
        ButtonView(
            onClick = {
                if (isValid) {
                    onClick()
                }
            },
            stringResource(id = R.string.next),
            isEnabled = (isValid)
        )
    } else {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            SpinKitLoadingIndicator()
        }

    }
}


@Composable
fun CardBankHolderTextField(addCardViewModel: AddBankCardViewModel = koinViewModel()) {
    NormalTextField(
        painter = painterResource(R.drawable.user_icon),
        label = stringResource(id = R.string.card_holder_name),
        value = addCardViewModel.holderNameTextValue.value,
        onValueChange = {
            if (it.text.length <= 100) {
                addCardViewModel.holderNameTextValue.value = it
                addCardViewModel.holderName.value = CardHolderName(it.text)
            }

        },
        error = addCardViewModel.holderName.collectAsState().value?.value?.fold({ stringResource(id = it) },
            { null }),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),

        )
}

object CardBankNumberMask {
    const val MASK = "####-####-####-####"
    const val LENGTH = 19
}

@Composable
fun CardBankNumberTextField(addCardViewModel: AddBankCardViewModel = koinViewModel()) {

    NormalTextField(
        painter = painterResource(R.drawable.credit_card_icon),
        label = stringResource(id = R.string.card_number),
        value = addCardViewModel.cardNumberTextValue.value,
        onValueChange = {
            if (it.text.length <= CardBankNumberMask.LENGTH) {
                addCardViewModel.cardNumberTextValue.value = it
                addCardViewModel.cardNumber.value = CardNumber(it.text.replace("-"," "))
            }
        },
        error = addCardViewModel.cardNumber.collectAsState().value?.value?.fold({ stringResource(id = it) },
            { null }),
        visualTransformation = MaskVisualTransformation(CardBankNumberMask.MASK),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
}

object ExpiryBankCardDateMask {
    const val MASK = "##/##"
    const val LENGTH = 4
}

@Composable
fun CardBankExpiryDateTextField(addCardViewModel: AddBankCardViewModel = koinViewModel()) {
    NormalTextField(
        painter = painterResource(R.drawable.calendar_icon),
        label = stringResource(id = R.string.expiry_date),
        value = addCardViewModel.expiryDateTextValue.value,
        onValueChange = {
            if (it.text.length <= ExpiryBankCardDateMask.LENGTH) {
                addCardViewModel.expiryDateTextValue.value = it
                addCardViewModel.expiryDate.value = CardExpiryDate(it.text)
            }
        },
        error = addCardViewModel.expiryDate.collectAsState().value?.value?.fold({ stringResource(id = it) },
            { null }),
        visualTransformation = MaskVisualTransformation(ExpiryBankCardDateMask.MASK),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
fun CardBankCvvTextField(addCardViewModel: AddBankCardViewModel = koinViewModel()) {
    NormalTextField(
        painter = painterResource(R.drawable.cvv_icon),
        label = stringResource(id = R.string.cvv),
        value = addCardViewModel.cvvTextValue.value,
        onValueChange = {
            if (it.text.length <= 3) {
                addCardViewModel.cvvTextValue.value = it
                addCardViewModel.cvv.value = CVV(it.text)
            }
        },
        error = addCardViewModel.cvv.collectAsState().value?.value?.fold({ stringResource(id = it) },
            { null }),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}
