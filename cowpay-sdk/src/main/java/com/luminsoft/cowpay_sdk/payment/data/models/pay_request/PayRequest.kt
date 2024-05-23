package com.luminsoft.cowpay_sdk.payment.data.models.pay_request

import com.google.gson.annotations.SerializedName

open class PayRequest {
    @SerializedName("gatewayTargetMethod")
    internal var gatewayTargetMethod: String? = null

    @SerializedName("merchantReferenceId")
    internal var merchantReferenceId: String? = null

    @SerializedName("customerMerchantProfileId")
    internal var customerMerchantProfileId: String? = null

    @SerializedName("amount")
    internal var amount: Number? = null

    @SerializedName("signature")
    internal var signature: String? = null

    @SerializedName("customerMobile")
    internal var customerMobile: String? = null

    @SerializedName("customerEmail")
    internal var customerEmail: String? = null

    @SerializedName("description")
    internal var description: String? = null

    @SerializedName("isfeesOnCustomer")
    internal var isFeesOnCustomer: Boolean? = null

    @SerializedName("customerFirstName")
    internal var customerFirstName: String? = null

    @SerializedName("customerLastName")
    internal var customerLastName: String? = null


    @SerializedName("customerAddress")
    internal var customerAddress: String? = "Big Street"

    @SerializedName("customerCountry")
    internal var customerCountry: String? = "US"

    @SerializedName("customerState")
    internal var customerState: String? = "CAS"

    @SerializedName("customerCity")
    internal var customerCity: String? = "City"

    @SerializedName("customerZip")
    internal var customerZip: String? = "123456"

    @SerializedName("customerIP")
    internal var customerIP: String? = "123.123.123.123"

    @SerializedName("channelType")
    internal var channelType: Int? = 1

    @SerializedName("cardNumber")
    internal var cardNumber: String? = null

    @SerializedName("cardCvv")
    internal var cvv: String? = null

    @SerializedName("cardExpYear")
    internal var expiryYear: String? = null

    @SerializedName("cardExpMonth")
    internal var expiryMonth: String? = null

    @SerializedName("cardHolderName")
    internal var cardHolder: String? = null

    @SerializedName("returnUrl3DS")
    internal var returnUrl3DS: String? = null

    @SerializedName("isTokenized")
    internal var isTokenized: Boolean? = null

    @SerializedName("tokenId")
    internal var tokenId: String? = null

}
