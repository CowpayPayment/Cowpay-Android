package com.luminsoft.cowpay_sdk.ui.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.luminsoft.cowpay_sdk.R

@Composable
fun BackButtonView(navController: NavController, onClick: (() -> Unit)?=null) {
    Button(
        onClick = {

            if (onClick == null) {
                navController.navigateUp()
            }else{
                onClick()
            }
        },
        contentPadding = PaddingValues(0.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .width(70.dp)
                .fillMaxHeight()
        ) {

            Spacer(
                modifier = Modifier
                    .width(5.dp)
            )
            Image(
                painterResource(R.drawable.back_icon),
                contentDescription = "",
                modifier = Modifier
                    .size(20.dp),
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
            )
            Spacer(
                modifier = Modifier
                    .width(10.dp)
            )
            Image(
                painterResource(R.drawable.cowpaylogo),
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp),
                colorFilter = ColorFilter.tint(color = Color.White)
            )

        }

    }
}