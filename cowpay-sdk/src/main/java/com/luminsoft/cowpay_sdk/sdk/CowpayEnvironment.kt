package com.luminsoft.cowpay_sdk.sdk



enum class CowpayEnvironment {
    STAGING,PRODUCTION;

    fun getTokenUrl(): String {
        return when (this) {
            STAGING -> {
                CowpaySDK.identityStagingBaseUrl
            }

            PRODUCTION -> {
                CowpaySDK.identityProductionBaseUrl
            }

        }
    }
    fun getRedirectBaseUrl(): String {
        return when (this) {
            STAGING -> {
                CowpaySDK.getApisUrl()
            }

            PRODUCTION -> {
                CowpaySDK.redirectBaseUrl
            }

        }
    }
}