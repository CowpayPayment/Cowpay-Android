package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.saved_cards.ui.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.luminsoft.cowpay_sdk.R
import com.luminsoft.cowpay_sdk.failures.AuthFailure
import com.luminsoft.cowpay_sdk.failures.ServerFailure
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.TokenizedCardData
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.saved_cards.view_model.SavedCardsViewModel
import com.luminsoft.cowpay_sdk.payment.presentation.ui.PaymentTotalDetails
import com.luminsoft.cowpay_sdk.sdk.CowpaySDK
import com.luminsoft.cowpay_sdk.sdk.model.PaymentFailedModel
import com.luminsoft.cowpay_sdk.ui.components.BackGroundView
import com.luminsoft.cowpay_sdk.ui.components.BottomSheet
import com.luminsoft.cowpay_sdk.ui.components.BottomSheetStatus
import com.luminsoft.cowpay_sdk.ui.components.ButtonView
import com.luminsoft.cowpay_sdk.ui.components.LoadingView
import com.luminsoft.cowpay_sdk.ui.components.SpinKitLoadingIndicator
import com.luminsoft.cowpay_sdk.utils.SdkNavigation
import org.koin.androidx.compose.koinViewModel


@Composable
fun SavedCardsContent(
    savedCardsViewModel: SavedCardsViewModel = koinViewModel<SavedCardsViewModel>(),
    navController: NavController
) {
    val loading = savedCardsViewModel.loading.collectAsState()
    savedCardsViewModel.navController = navController
    val failure = savedCardsViewModel.failure.collectAsState()
    val savedCards = savedCardsViewModel.savedCards.collectAsState()
    val feesResponse = savedCardsViewModel.feesResponse.collectAsState()
    val selectedCard = savedCardsViewModel.selectedCard.collectAsState()
    val isButtonLoading = savedCardsViewModel.isButtonLoading.collectAsState()
    val isShowCvvDialog = savedCardsViewModel.isCvvBottomSheetOpen.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    if (loading.value) {
        LoadingView()
    } else if (!loading.value && feesResponse.value != null) {
        BackGroundView(
            title = stringResource(id = R.string.saved_cards),
            navController = navController,
            isBackButton = true,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
               Column(modifier = Modifier.fillMaxHeight().weight(1f)) {
                   Column() {
                       Row(
                           modifier = Modifier
                               .fillMaxWidth()
                               .height(30.dp),
                           horizontalArrangement = Arrangement.SpaceBetween,
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           Text(
                               stringResource(id = R.string.select_card),
                               style = MaterialTheme.typography.labelLarge,
                               color = MaterialTheme.colorScheme.onSurface
                           )
                           Button(
                               onClick = {
                                   var isSavedCards = true
                                   navController.navigate(
                                       "${SdkNavigation.AddCardScreen.route}/{isSavedCards}".replace(
                                           oldValue = "{isSavedCards}",
                                           newValue = "$isSavedCards"
                                       )
                                   )
                               },
                               modifier = Modifier
                                   .height(30.dp),
                               colors = ButtonDefaults.buttonColors(containerColor = Color.Unspecified),
                               contentPadding = PaddingValues(0.dp),
                           ) {
                               Row(verticalAlignment = Alignment.CenterVertically) {
                                   Image(
                                       painterResource(R.drawable.add_icon),
                                       contentDescription = "",
                                       modifier = Modifier
                                           .size(15.dp),
                                   )
                                   Spacer(modifier = Modifier.width(5.dp))
                                   Text(
                                       stringResource(id = R.string.add_new_card),
                                       style = MaterialTheme.typography.labelLarge,
                                       color = MaterialTheme.colorScheme.primary
                                   )
                               }
                           }

                       }
                       Spacer(modifier = Modifier.height(5.dp))
                   }
                   if (savedCards.value!!.isNotEmpty()) {
                       LazyColumn(modifier = Modifier.weight(1f)) {

                           items(savedCards.value!!) { cardData ->
                               cardItem(cardData, onClick = {
                                   savedCardsViewModel.changeSelectedCard(cardData)
                               }, isSelected = (cardData == selectedCard.value))
                           }
                       }
                   }
                   else {
                       Row(
                           modifier = Modifier
                               .fillMaxWidth()
                               .fillMaxHeight(),
                           horizontalArrangement = Arrangement.Center,
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           Text(
                               stringResource(id = R.string.there_is_no_saved_cards),
                               style = MaterialTheme.typography.labelSmall,
                               color = MaterialTheme.colorScheme.onSurface
                           )
                       }
                   }
               }
                if (!savedCards.value.isNullOrEmpty())
                        Column() {
                            Spacer(modifier = Modifier.height(10.dp))
                            feesResponse.value?.let {
                                CowpaySDK.paymentInfo?.amount?.let { it1 ->
                                    PaymentTotalDetails(
                                        it,
                                        it1, CowpaySDK.paymentInfo!!.isFeesOnCustomer
                                    )
                                }
                            }
                            if (!isButtonLoading.value) {
                                ConfirmButton(
                                    onClick = {
                                        savedCardsViewModel.isCvvBottomSheetOpen.value = true
                                    })
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    SpinKitLoadingIndicator()
                                }

                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }

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
                            CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))

                        },
                    )
                    {
                        activity.finish()
                        CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
                    }
                }
            } else {
                failure.value?.let {
                    BottomSheet(
                        bottomSheetStatus = BottomSheetStatus.ERROR,
                        text = it.message,
                        buttonText = stringResource(id = R.string.retry),
                        onPressedButton = {
                            savedCardsViewModel.retry()
                        },
                        secondButtonText = stringResource(id = R.string.exit),
                        onPressedSecondButton = {
                            activity.finish()
                            CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
                        }
                    )
                    {
                        activity.finish()
                        CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
                    }
                }

            }
        }
        if (isShowCvvDialog.value) {
            CvvDialog()
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
                        CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
                    },
                )
                {
                    activity.finish()
                    CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))
                }
            }
        } else if (feesResponse.value == null) {
            failure.value?.let {
                BottomSheet(
                    bottomSheetStatus = BottomSheetStatus.ERROR,
                    text = it.message,
                    buttonText = stringResource(id = R.string.retry),
                    onPressedButton = {
                        savedCardsViewModel.retry()
                    },
                    secondButtonText = stringResource(id = R.string.exit),
                    onPressedSecondButton = {
                        activity.finish()
                        CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))

                    }
                )
                {
                    activity.finish()
                    CowpaySDK.cowpayCallback?.error(PaymentFailedModel(it.message,it))

                }
            }

        }
    }
}

@Composable
fun cardItem(tokenizedCardData: TokenizedCardData, onClick: () -> Unit, isSelected: Boolean) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Unspecified),
        contentPadding = PaddingValues(0.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .height(60.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp))
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
                    shape = RoundedCornerShape(20.dp),
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Row (modifier = Modifier
                .fillMaxHeight(),verticalAlignment = Alignment.CenterVertically){
                tokenizedCardData.parseTokenizedCardNetworkTypeEnum()?.let {
                    Image(
                        painterResource(it.getImageId()),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .size(30.dp),
                    )
                }
                Text(
                    "**** **** **** ${tokenizedCardData.cardNumber?.substring((tokenizedCardData.cardNumber?.length ?:4) -4) ?: ""}",
                    modifier = Modifier.padding(start = 15.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(modifier = Modifier
                .fillMaxHeight(),verticalAlignment = Alignment.CenterVertically){
                Text(
                    "${tokenizedCardData.cardExpMonth}/${tokenizedCardData.cardExpYear}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                RadioButton(
                    selected = (isSelected), modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp),
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

}

@Composable
fun ConfirmButton(
    savedCardsViewModel: SavedCardsViewModel = koinViewModel<SavedCardsViewModel>(),
    onClick: () -> Unit
) {
    val isValid: Boolean = (savedCardsViewModel.selectedCard.collectAsState().value != null)

    val isButtonLoading = savedCardsViewModel.isButtonLoading.collectAsState()

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

