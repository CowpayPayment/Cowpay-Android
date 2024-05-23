package com.luminsoft.cowpay_sdk.form_fields

import arrow.core.Either
import com.luminsoft.cowpay_sdk.R


class CardNumber(text:String) {
     var value: Either<Int, CardNumberData>? = null
    init {
        value = validate(text)
    }
     private fun validate(value:String):Either<Int, CardNumberData> {
         val isNumber = value.matches(Regex("((?:[0-9]+ ?){4,})$"))

          if (isNumber) {
             val trimmedValue: String = value.replace(" ","")
              val isMasterCard = trimmedValue.matches(Regex("(5[1-5][0-9]{14}|2(22[1-9][0-9]{12}|2[3-9][0-9]{13}|[3-6][0-9]{14}|7[0-1][0-9]{13}|720[0-9]{12}))\$"))
              val isVisa = trimmedValue.matches(Regex("4[0-9]{12}(?:[0-9]{3})?$"))
              return if(isMasterCard){
                  Either.Right(CardNumberData(value,CardType.MasterCard))
              }else if (isVisa){
                  Either.Right(CardNumberData(value,CardType.VisaCard))
              }else{
                  Either.Left( R.string.invalidCardNumber)
              }
         } else if (value.isEmpty()) {
              return  Either.Left(R.string.requiredCardNumber)
         } else {
              return  Either.Left( R.string.invalidCardNumber)
         }
    }
}
enum class CardType { MasterCard, VisaCard}

data class CardNumberData (val number:String,val cardType:CardType)