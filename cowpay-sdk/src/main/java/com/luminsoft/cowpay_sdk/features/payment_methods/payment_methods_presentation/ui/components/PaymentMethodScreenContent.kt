package com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_presentation.ui.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.luminsoft.cowpay_sdk.R
import com.luminsoft.cowpay_sdk.failures.AuthFailure
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_presentation.view_model.PaymentMethodsViewModel
import com.luminsoft.cowpay_sdk.payment.data.models.PaymentOption
import com.luminsoft.cowpay_sdk.sdk.CowpaySDK
import com.luminsoft.cowpay_sdk.sdk.model.PaymentFailedModel
import com.luminsoft.cowpay_sdk.ui.components.BackGroundView
import com.luminsoft.cowpay_sdk.ui.components.BottomSheet
import com.luminsoft.cowpay_sdk.ui.components.BottomSheetStatus
import com.luminsoft.cowpay_sdk.ui.components.ButtonView
import com.luminsoft.cowpay_sdk.ui.components.LoadingView
import com.luminsoft.cowpay_sdk.utils.SdkNavigation
import org.koin.androidx.compose.koinViewModel


@Composable
fun PaymentMethodScreenContent(
    paymentMethodsViewModel: PaymentMethodsViewModel = koinViewModel(),
    navController: NavController
) {
    val loading = paymentMethodsViewModel.loading.collectAsState()
    val failure = paymentMethodsViewModel.failure.collectAsState()
    val methodsResponse = paymentMethodsViewModel.methodsResponse.collectAsState()
    val selectedPaymentOption = paymentMethodsViewModel.selectedPaymentOption.collectAsState()
    val activity = LocalContext.current as Activity

    if (loading.value) {
        LoadingView()
    } else if (!loading.value && failure.value == null && methodsResponse.value != null) {
        BackGroundView(
            title = stringResource(id = R.string.payment_methods),
            navController = navController,
            isBackButton = true,
            onClick = {
                activity.finish()
                CowpaySDK.cowpayCallback?.closedByUser()

            }) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    if (!CowpaySDK.merchantLogo.isNullOrEmpty()) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = CowpaySDK.merchantLogo,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(100.dp),
                                )

                            }
                            Divider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                    Text(
                        stringResource(id = R.string.select_payment_methods),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    if (methodsResponse.value!!.isNotEmpty())
                        LazyColumn {
                            items(methodsResponse.value!!) { method ->
                                OptionItemVIew(method, onClick = {
                                    paymentMethodsViewModel.changeMethodOption(method)
                                }, isSelected = (method == selectedPaymentOption.value))
                            }
                        } else {
                        Text(
                            stringResource(id = R.string.select_payment_methods),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Column {
                    ButtonView(
                        onClick = {
                            if (selectedPaymentOption.value != null) {

                                when (selectedPaymentOption.value) {
                                    PaymentOption.FawryPay -> {
                                        navController.navigate(SdkNavigation.FawryPayScreen.route)
                                    }

                                    PaymentOption.CreditCard -> {
                                        navController.navigate(SdkNavigation.SavedCardScreen.route)

                                    }

                                    PaymentOption.BankCard -> {
                                        navController.navigate(SdkNavigation.AddBankCardScreen.route)

                                    }

                                    else -> {}
                                }
                            }
                        },
                        stringResource(id = R.string.next),
                        isEnabled = (selectedPaymentOption.value != null)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
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
        } else {
            failure.value?.let {
                BottomSheet(
                    bottomSheetStatus = BottomSheetStatus.ERROR,
                    text = it.message,
                    buttonText = stringResource(id = R.string.retry),
                    onPressedButton = {
                        paymentMethodsViewModel.retry()
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
fun OptionItemVIew(paymentOption: PaymentOption, onClick: () -> Unit, isSelected: Boolean) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Unspecified),
        contentPadding = PaddingValues(0.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .height(45.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.04f))
                .border(
                    isSelected.let {
                        if (it) 1.dp else 0.dp
                    },
                    isSelected.let {
                        if (it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.04f
                        )
                    },
                    shape = RoundedCornerShape(15.dp),
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Row {
                Image(
                    painterResource(paymentOption.getImageID()),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .size(25.dp),
                )
                Text(
                    stringResource(id = paymentOption.getName()),
                    modifier = Modifier.padding(start = 15.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            RadioButton(
                selected = (isSelected), modifier = Modifier.padding(end = 15.dp),
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.1f
                    )
                ),
            )
        }
    }
}

