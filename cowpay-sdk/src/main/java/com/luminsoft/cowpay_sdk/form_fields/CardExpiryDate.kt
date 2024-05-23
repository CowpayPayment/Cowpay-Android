package com.luminsoft.cowpay_sdk.form_fields

import arrow.core.Either
import com.luminsoft.cowpay_sdk.R
import java.text.SimpleDateFormat
import java.util.Date


class CardExpiryDate(text:String) {
     var value: Either<Int, String>? = null
    init {
        value = validate(text)
    }
     private fun validate(value:String):Either<Int, String> {

          if (value.length == 4) {
              val expiryString = value.replaceRange(2, 2, "/")
              val isExpiryValid =
                  expiryString.matches(Regex("(0[1-9]|10|11|12)/([0-9]{4}|[0-9]{2})$"))
              if (isExpiryValid) {
                  val sdf = SimpleDateFormat("01/MM/yyyy")
                  val currentDateString = sdf.format(Date())
                 val currentDate = sdf.parse(currentDateString)

                  val list = expiryString.split("/")
                  val expiry = "01/${list[0]}/20${list[1]}"
                  val expiryDate = sdf.parse(expiry)

                  val cmp = currentDate.compareTo(expiryDate)

                  return when {
                      cmp > 0 -> {
                          Either.Left(R.string.invalidCardExpiryOldDate)
                      }
                      cmp < 0 -> {
                          Either.Right(expiryString)
                      }
                      else -> {
                          Either.Right(value)
                      }
                  }

              }
              return  Either.Left( R.string.invalidCardExpiry)
         } else if (value.isEmpty()) {
              return Either.Left(R.string.requiredCardExpiry)
         } else {
              return  Either.Left( R.string.invalidCardExpiry)
         }
    }
}