package com.luminsoft.cowpay_sdk.payment.data.models

import com.luminsoft.cowpay_sdk.R

enum class PaymentOption {
    CreditCard, FawryPay, TokenizedCreditCard, BankCard;

    fun getName(): Int {
        return when (this) {
            FawryPay -> {
                R.string.fawry_pay
            }

            CreditCard -> {
                R.string.credit_card
            }

            TokenizedCreditCard -> {
                R.string.credit_card
            }

            BankCard -> {
                R.string.bank_card
            }
        }
    }

    fun getGatewayTargetMethod(): String {
        return when (this) {
            FawryPay -> {
                "PayAtFawry"
            }

            CreditCard -> {
                "CreditCard"
            }

            TokenizedCreditCard -> {
                "CreditCard"
            }

            BankCard -> {
                "MPGSCARD"
            }
        }
    }

    fun getId(): Int {
        return when (this) {
            CreditCard -> {
                1
            }

            TokenizedCreditCard -> {
                1
            }

            FawryPay -> {
                2
            }

            BankCard -> {
                12
            }
        }
    }

    fun getImageID(): Int {
        return when (this) {
            CreditCard -> {
                R.drawable.mastercard_logo
            }

            TokenizedCreditCard -> {
                R.drawable.mastercard_logo
            }

            BankCard -> {
                R.drawable.mastercard_logo
            }

            FawryPay -> {
                R.drawable.fawry_logo
            }
        }
    }
}

fun setPaymentOption(id: Int): PaymentOption? {
    return when (id) {
        1 -> {
            PaymentOption.CreditCard
        }

        2 -> {
            PaymentOption.FawryPay
        }

        12 -> {
            PaymentOption.BankCard
        }

        else -> {
            return null
        }
    }


}