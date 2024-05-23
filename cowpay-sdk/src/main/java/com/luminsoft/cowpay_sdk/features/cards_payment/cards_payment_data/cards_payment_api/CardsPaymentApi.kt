package com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_api


import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.TokenizedCardData
import com.luminsoft.cowpay_sdk.network.ApiBaseResponse
import com.luminsoft.cowpay_sdk.features.cards_payment.cards_payment_data.cards_payment_models.get_token.GetCardsRequest

import retrofit2.Response
import retrofit2.http.*

interface CardsPaymentApi {
    @POST("/payment/GetCreditCardDetails")
    suspend fun getCards(@Body request : GetCardsRequest): Response<ApiBaseResponse<ArrayList<TokenizedCardData>>>
}