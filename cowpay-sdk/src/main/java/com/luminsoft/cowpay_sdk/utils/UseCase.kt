package com.luminsoft.cowpay_sdk.utils

interface  UseCase<T,Params>{
     suspend fun call(params:Params):T
}