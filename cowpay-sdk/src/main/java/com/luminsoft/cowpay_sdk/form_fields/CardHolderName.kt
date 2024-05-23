package com.luminsoft.cowpay_sdk.form_fields

import arrow.core.Either
import com.luminsoft.cowpay_sdk.R


class CardHolderName(text:String) {
     var value: Either<Int, String>? = null
    init {
        value = validate(text)
    }
     private fun validate(value:String):Either<Int, String> {
        val isValid = value.matches(Regex("^(?:[A-Za-z]+ ?){1,4}$"))
         return if (isValid && (value.trim().split(" ").size >= 2)) {
             Either.Right(value)
         } else if (value.isEmpty()) {
             Either.Left(R.string.requiredCardHolderName)
         } else {
             Either.Left( R.string.invalidCardHolderName)
         }
    }
}