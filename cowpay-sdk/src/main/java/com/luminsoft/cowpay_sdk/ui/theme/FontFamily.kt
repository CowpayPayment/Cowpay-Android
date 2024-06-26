package com.luminsoft.cowpay_sdk.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.luminsoft.cowpay_sdk.R

val sdkFontFamily = FontFamily(
    fonts = arrayOf(
        Font(
            resId = R.font.poppins_bold,
            weight = FontWeight.W900,
            FontStyle.Normal
        ),
        Font(
            resId = R.font.poppins_medium,
            weight = FontWeight.W500,
            FontStyle.Normal
        ),
        Font(
            resId = R.font.poppins_semi_bold,
            weight = FontWeight.W600,
            FontStyle.Normal
        ),
        Font(
            resId = R.font.poppins_regular,
            weight = FontWeight.W400,
            FontStyle.Normal
        ),
    )
)
