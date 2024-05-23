package com.luminsoft.cowpay_sdk.payment.di

import com.luminsoft.cowpay_sdk.network.AuthInterceptor
import com.luminsoft.cowpay_sdk.network.RetroClient
import com.luminsoft.cowpay_sdk.payment.data.payment_api.PaymentApi
import com.luminsoft.cowpay_sdk.payment.data.payment_repository.PaymentRepositoryImplementation
import com.luminsoft.cowpay_sdk.payment.data.remote_data_source.PaymentRemoteDataSource
import com.luminsoft.cowpay_sdk.payment.data.remote_data_source.PaymentRemoteDataSourceImpl
import com.luminsoft.cowpay_sdk.payment.domain.repository.PaymentRepository
import com.luminsoft.cowpay_sdk.payment.domain.usecases.GenerateSignatureUseCase
import com.luminsoft.cowpay_sdk.payment.domain.usecases.GetFeesUseCase
import com.luminsoft.cowpay_sdk.payment.domain.usecases.PayUseCase
import org.koin.dsl.module

val paymentModule = module{
    single {
        GenerateSignatureUseCase()
    }
    single {
        PayUseCase(get())
    }
    single {
        GetFeesUseCase(get())
    }
    single<PaymentRemoteDataSource> {
        PaymentRemoteDataSourceImpl(get(),get())
    }
    single<PaymentRepository> {
        PaymentRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(PaymentApi::class.java)
    }


}