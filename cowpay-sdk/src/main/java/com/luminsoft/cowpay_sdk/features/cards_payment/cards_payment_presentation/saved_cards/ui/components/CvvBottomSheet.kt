package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.saved_cards.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.luminsoft.cowpay_sdk.R
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_presentation.saved_cards.view_model.SavedCardsViewModel
import com.luminsoft.cowpay_sdk.form_fields.CVV
import com.luminsoft.cowpay_sdk.ui.components.ButtonView
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CvvDialog(
    savedCardsViewModel: SavedCardsViewModel = koinViewModel<SavedCardsViewModel>(),
) {

    val isValid: Boolean = savedCardsViewModel.cvv.collectAsState().value?.value?.isRight() ?: false

    AlertDialog(
        onDismissRequest = {
            savedCardsViewModel.isCvvBottomSheetOpen.value = false
        },
                modifier = Modifier.clip(shape = RoundedCornerShape(15.dp)).background(color = MaterialTheme.colorScheme.onPrimary),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth().clip(shape = RoundedCornerShape(15.dp)).background(color = MaterialTheme.colorScheme.onPrimary).padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                stringResource(id = R.string.type_card_cvv_number),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            cvvTextField()

            Spacer(modifier = Modifier.height(40.dp))
            ButtonView(
                isEnabled = isValid,
                onClick = {
                    if (isValid) {
                        savedCardsViewModel.isCvvBottomSheetOpen.value = false
                        savedCardsViewModel.pay()
                    }
                },
                title = stringResource(id = R.string.pay_now)
            )
            Spacer(modifier = Modifier.height(10.dp))

        }


    }
}
@Composable
fun cvvTextField(savedCardsViewModel: SavedCardsViewModel = koinViewModel<SavedCardsViewModel>(),){

     val error = savedCardsViewModel.cvv.collectAsState().value?.value?.fold({ stringResource(id = it) },
        { null })

    TextField(
        value = savedCardsViewModel.cvvTextValue.value,
        onValueChange = {
            if (it.text.length <= 3) {
                savedCardsViewModel.cvvTextValue.value = it
                savedCardsViewModel.cvv.value = CVV(it.text)
            }
        },
        textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center, fontSize = 20.sp),
        modifier = Modifier
            .width(70.dp),
        isError = (error != null),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
            errorIndicatorColor = MaterialTheme.colorScheme.error,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            errorCursorColor = MaterialTheme.colorScheme.error,
            errorTextColor = MaterialTheme.colorScheme.primary,
        ),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number,imeAction = ImeAction.Done)

    )
    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

