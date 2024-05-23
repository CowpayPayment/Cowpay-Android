package com.luminsoft.cowpay_sdk.payment.domain.usecases

import com.luminsoft.cowpay_sdk.utils.UseCase
import java.security.MessageDigest

class GenerateSignatureUseCase: UseCase<String, GenerateSignatureUseCaseParams> {

    override suspend fun call(params: GenerateSignatureUseCaseParams): String {
        val str =
            "${params.merchantCode}${params.merchantReferenceId}${params.customerMerchantProfileId}${params.amount}${params.hashKey}"
        val bytes = str.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}

data class GenerateSignatureUseCaseParams(
    val merchantCode:String,
    val merchantReferenceId:String,
    val customerMerchantProfileId:String,
    val amount :Number,
    val hashKey:String
    )