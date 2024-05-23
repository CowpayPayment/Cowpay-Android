package com.luminsoft.cowpay_sdk.features.fawry_payment.di

import com.luminsoft.cowpay_sdk.features.fawry_payment.fawry_payment_presentation.view_model.FawryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val fawryModule = module{
    viewModel{
        FawryViewModel(get(),get())
    }
}