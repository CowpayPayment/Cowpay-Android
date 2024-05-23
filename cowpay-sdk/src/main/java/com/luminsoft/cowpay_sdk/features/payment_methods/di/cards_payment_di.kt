package com.luminsoft.cowpay_sdk.features.payment_methods.di

import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.usecases.GetPaymentMethodsUseCase
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.usecases.GetTokenUseCase
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_presentation.view_model.PaymentMethodsViewModel
import com.luminsoft.cowpay_sdk.network.AuthInterceptor
import com.luminsoft.cowpay_sdk.network.RetroClient
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_api.PaymentMethodsApi
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_repository.PaymentMethodsRepositoryImplementation
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_remote_data_source.PaymentMethodsRemoteDataSource
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_data.payment_methods_remote_data_source.PaymentMethodsRemoteDataSourceImpl
import com.luminsoft.cowpay_sdk.features.payment_methods.payment_methods_domain.repository.PaymentMethodsRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val paymentMethodsModule = module {

    single {
        GetTokenUseCase(get())
    }
    single {
        GetPaymentMethodsUseCase(get())
    }
    single<PaymentMethodsRemoteDataSource> {
        PaymentMethodsRemoteDataSourceImpl(get(), get())
    }
    single<PaymentMethodsRepository> {
        PaymentMethodsRepositoryImplementation(get())
    }
    single {
        RetroClient.provideRetrofit(
            RetroClient.provideOkHttpClient(
                AuthInterceptor()
            )
        ).create(PaymentMethodsApi::class.java)
    }
    viewModel {
        PaymentMethodsViewModel(get(),get())
    }

}