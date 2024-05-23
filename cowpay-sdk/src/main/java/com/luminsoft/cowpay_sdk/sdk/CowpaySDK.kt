package com.luminsoft.cowpay_sdk.sdk

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import com.luminsoft.cowpay_sdk.sdk.model.PaymentInfo
import com.luminsoft.cowpay_sdk.CowpayMainActivity
import com.luminsoft.cowpay_sdk.sdk.model.CowpayCallback
import java.util.Locale

object CowpaySDK {
    // this info related to organization
    internal var merchantCode = ""
    internal var merchantPhoneNumber = ""
    internal var hashKey = ""
    var merchantLogo: String? = null

    // this info related to every order
    internal var paymentInfo: PaymentInfo? = null
    var environment = CowpayEnvironment.STAGING
    private var localizationCode = LocalizationCode.EN
    internal var cowpayCallback: CowpayCallback? = null
    internal var identityProductionBaseUrl: String = "https://identity.cowpay.me:8002/GetToken"
    internal var identityStagingBaseUrl: String = "https://sit.cowpay.me:8000/identity/GetToken"
    internal var redirectBaseUrl: String = "https://dashboard.cowpay.me:8070/"

    internal fun getBaseUrl(): String {
        return if (environment == CowpayEnvironment.STAGING)
            "https://sit.cowpay.me"
        else "https://apigateway.cowpay.me"
    }

    internal fun getApisUrl(): String {
        return if (environment == CowpayEnvironment.STAGING)
            getBaseUrl() + ":8000"
        else getBaseUrl() + ":8000"
    }

    @Throws(Exception::class)
    fun init(
        merchantCode: String,
        merchantHash: String,
        merchantPhoneNumber: String,
        paymentInfo: PaymentInfo,
        environment: CowpayEnvironment = CowpayEnvironment.STAGING,
        localizationCode: LocalizationCode = LocalizationCode.EN,
        merchantLogo: String? = null,
    ) {
        if (merchantCode.isEmpty())
            throw Exception("Invalid Merchant Code")
        if (merchantHash.isEmpty())
            throw Exception("Invalid Hash Key")
        if (merchantPhoneNumber.isEmpty())
            throw Exception("Invalid Merchant PhoneNumber Key")
//        if (!paymentInfo.customerMobile.matches(Regex("(01[0-9]{9})\$")))
        if (paymentInfo.customerMobile.isEmpty())
            throw Exception("Invalid customer mobile number")
        this.environment = environment
        this.merchantCode = merchantCode
        this.hashKey = merchantHash
        this.paymentInfo = paymentInfo
        this.merchantPhoneNumber = merchantPhoneNumber
        this.localizationCode = localizationCode
        this.merchantLogo = merchantLogo

    }

    fun launch(
        activity: Activity,
        cowpayCallback: CowpayCallback? = null,
    ) {
        if (this.merchantCode.isEmpty())
            throw Exception("Invalid Merchant Code")
        if (this.hashKey.isEmpty())
            throw Exception("Invalid Hash Key")
        if (this.merchantPhoneNumber.isEmpty())
            throw Exception("Invalid Merchant PhoneNumber Key")
        if (this.paymentInfo == null)
            throw Exception("Invalid PaymentInfo")
//        if (this.paymentInfo?.customerMobile?.matches(Regex("(01[0-9]{9})\$")) != true)
        if (this.paymentInfo?.customerMobile.isNullOrEmpty())
            throw Exception("Invalid customer mobile number")
        this.cowpayCallback = cowpayCallback
        setLocale(this.localizationCode, activity)
        activity.startActivity(Intent(activity, CowpayMainActivity::class.java))
    }

    @Suppress("DEPRECATION")
    private fun setLocale(lang: LocalizationCode, activity: Activity) {
        val locale = if (lang != LocalizationCode.AR) {
            Locale("en")
        } else {
            Locale("ar")
        }

        val config: Configuration = activity.baseContext.resources.configuration
        config.setLocale(locale)
        activity.baseContext.resources.updateConfiguration(
            config,
            activity.baseContext.resources.displayMetrics
        )
    }

}