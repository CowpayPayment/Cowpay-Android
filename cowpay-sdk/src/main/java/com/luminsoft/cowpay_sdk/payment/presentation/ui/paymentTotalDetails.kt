package com.luminsoft.cowpay_sdk.payment.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.luminsoft.cowpay_sdk.R
import com.luminsoft.cowpay_sdk.payment.data.models.get_fees.GetFeesResponse

@Composable
fun PaymentTotalDetails(
    feesResponse: GetFeesResponse,
    subTotal: Number,
    isFeesOnCustomer: Boolean
) {
    var total = subTotal.toDouble()
    if (isFeesOnCustomer) {
        total = subTotal.toDouble() + (feesResponse.feesValue ?: 0).toDouble() + (feesResponse.vatValue ?: 0).toDouble()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(id = R.string.total_payment_amount),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "${String.format("%.2f", total)} ${stringResource(id = R.string.currency)}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (isFeesOnCustomer) {
            Column() {
                Divider(
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
                buildRowDetails(stringResource(id = R.string.subtotal), "$subTotal")
                if ((feesResponse.feesValue ?: 0).toDouble() > 0)
                    buildRowDetails(stringResource(id = R.string.fees), "${feesResponse.feesValue}")
                if ((feesResponse.vatValue ?: 0).toDouble() > 0)
                    buildRowDetails(stringResource(id = R.string.vat), "${feesResponse.vatValue}")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun buildRowDetails(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            "$value ${stringResource(id = R.string.currency)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}