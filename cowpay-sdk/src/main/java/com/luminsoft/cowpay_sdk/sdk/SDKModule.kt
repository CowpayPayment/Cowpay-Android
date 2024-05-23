package com.luminsoft.cowpay_sdk.sdk
import com.luminsoft.cowpay_sdk.network.BaseRemoteDataSource

import org.koin.dsl.module

val appModule = module{
    factory  {
        BaseRemoteDataSource()
    }

}