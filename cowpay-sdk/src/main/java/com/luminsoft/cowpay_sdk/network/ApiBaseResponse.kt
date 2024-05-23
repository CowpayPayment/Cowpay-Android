package com.luminsoft.cowpay_sdk.network

import android.provider.Contacts.People
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import java.util.Map


open class ApiBaseResponse<T> (
    @SerializedName("statusCode") val statusCode: Int? = 0,
    @SerializedName("operationStatus") val operationStatus: Int? = 0,
    @SerializedName("operationMessage") val operationMessage: String?="",
    @SerializedName("data")
    @Expose
    val data:T
)