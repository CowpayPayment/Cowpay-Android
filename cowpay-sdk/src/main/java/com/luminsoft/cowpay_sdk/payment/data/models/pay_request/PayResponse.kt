package com.luminsoft.cowpay_sdk.payment.data.models.pay_request

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PayResponse(
    @SerializedName("recurringToken") internal val recurringToken: String? = null,
    @SerializedName("status") internal val status: String? = null,
    @SerializedName("redirectMethod") internal val redirectMethod: String? = null,
    @SerializedName("tokenId") internal val tokenId: String? = null,
    @SerializedName("returnUrl3DS") internal val returnUrl3DS: String? = null,
    @SerializedName("redirectUrl") internal val redirectUrl: String? = null,
    @SerializedName("threeDSUrl") internal val threeDSUrl: String? = null,
    @SerializedName("paymentGatewayReferenceId") internal val paymentGatewayReferenceId: String? = null,
    @SerializedName("merchantReferenceId") internal val merchantReferenceId: String? = null,
    @SerializedName("merchantCode") internal val merchantCode: String? = null,
    @SerializedName("cowpayReferenceId") internal val cowpayReferenceId: String? = null,
    @SerializedName("amount") internal val amount: String? = null,
    @SerializedName("merchantAmount") internal val merchantAmount: String? = null,
    @SerializedName("feesAmount") internal val feesAmount: String? = null,
    @SerializedName("vatAmount") internal val vatAmount: String? = null,
    @SerializedName("paymentMethod") internal val paymentMethod: String? = null,
    @SerializedName("statusId") internal val statusId: String? = null,
    @SerializedName("reason") internal val reason: String? = null,
    @SerializedName("html") internal val html: String? = null,
    @SerializedName("customerReferenceId") internal val customerReferenceId: String? = null,
    @SerializedName("redirectParams") internal var redirectParams: RedirectParams? = RedirectParams(),

) : Serializable {
    fun getRedirectStatus(): RedirectStatusEnum {
        return when (status) {
            "3DS" -> RedirectStatusEnum.ThreeDS
            "REDIRECT" -> RedirectStatusEnum.Redirect
            "Pending" -> RedirectStatusEnum.Pending
            "Paid" -> RedirectStatusEnum.Paid
            "UnPaid" -> RedirectStatusEnum.Unpaid
            "Expired" -> RedirectStatusEnum.Expired
            "Failed" -> RedirectStatusEnum.Failed
            "Refunded" -> RedirectStatusEnum.Refunded
            "PartiallyRefunded" -> RedirectStatusEnum.PartiallyRefunded
            else -> {
                return RedirectStatusEnum.Unpaid
            }
        }
    }
    fun getRedirectMethods(): RedirectMethodsEnum {
        return when (redirectMethod) {
            "POST" -> RedirectMethodsEnum.POST
            "GET" -> RedirectMethodsEnum.GET
            else -> {
                return RedirectMethodsEnum.POST
            }
        }

    }
}

data class RedirectParams(
    @SerializedName("paReq") var paReq: String? = null,
    @SerializedName("redirect") var redirect: String? = null,
    @SerializedName("termUrl") var termUrl: String? = null,
    @SerializedName("md") var md: String? = null,
    @SerializedName("body") var body: String? = null
):Serializable

enum class RedirectStatusEnum {
    ThreeDS,
    Redirect,
    Pending,
    Paid,
    Unpaid,
    Expired,
    Failed,
    Refunded,
    PartiallyRefunded
}

enum class RedirectMethodsEnum {
    POST,
    GET
}