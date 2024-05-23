package com.luminsoft.cowpay_sdk.form_fields

import arrow.core.Either
import com.luminsoft.cowpay_sdk.R


class CVV(text:String) {
     var value: Either<Int, String>? = null
    init {
        value = validate(text)
    }
     private fun validate(value:String):Either<Int, String> {
        val isValid = value.matches(Regex("[0-9]{3}\$"))
         return if (isValid) {
             Either.Right(value)
         } else if (value.isEmpty()) {
             Either.Left(R.string.requiredCardCvv)
         } else {
             Either.Left( R.string.invalidCardCvv)
         }
    }
}