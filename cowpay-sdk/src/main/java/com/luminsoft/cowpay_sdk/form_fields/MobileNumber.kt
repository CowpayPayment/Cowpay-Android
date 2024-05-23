package com.luminsoft.cowpay_sdk.form_fields

import arrow.core.Either
import com.luminsoft.cowpay_sdk.R
import com.togitech.ccp.component.isPhoneNumber


class MobileNumber(text: String) {
    var value: Either<Int, String>? = null

    init {
        value = validate(text)
    }

    private fun validate(
        value: String,

        ): Either<Int, String> {

//        if(value.isEmpty())
//            return Either.Right(value);
        return if (!isPhoneNumber()) {
            Either.Right(value)
        } else if (value.isEmpty()) {
            Either.Left(R.string.requiredMobileNumber)
        } else {
            Either.Left(R.string.invalidMobileNumber)
        }
    }
}