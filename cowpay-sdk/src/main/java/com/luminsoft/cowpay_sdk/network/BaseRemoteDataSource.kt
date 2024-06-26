package com.luminsoft.cowpay_sdk.network

import com.google.gson.Gson
import com.luminsoft.cowpay_sdk.failures.AuthFailure
import com.luminsoft.cowpay_sdk.failures.NetworkFailure
import com.luminsoft.cowpay_sdk.failures.NoConnectionFailure
import com.luminsoft.cowpay_sdk.failures.ServerFailure
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

@Suppress("UNCHECKED_CAST")
class BaseRemoteDataSource {

    private val gson = Gson()

    suspend fun <T> apiRequest(
        call: suspend () -> Response<T>
    ): BaseResponse<Any> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                val body = response.body() ?: {}
                if ((response.code() == 200 || response.code() == 201))
                    return BaseResponse.Success(body)
            } else {
                return if (response.code() == 401) {
                    BaseResponse.Error(
                        AuthFailure()
                    )

                } else {
                    BaseResponse.Error(
                        ServerFailure(
                            gson.fromJson(
                                response.errorBody()?.string() ?: "Some Thing went wrong",
                                ApiErrorResponse::class.java
                            )
                        )
                    )
                }
            }

        } catch (e: Exception) {
            return when (e) {
                is NoConnectionException -> {
                    BaseResponse.Error(
                        NoConnectionFailure()
                    )
                }

                is UnknownHostException -> {
                    BaseResponse.Error(
                        NetworkFailure()
                    )
                }

                is ConnectException -> {
                    BaseResponse.Error(
                        NetworkFailure()
                    )
                }

                else -> {
                    BaseResponse.Error(
                        NetworkFailure(e.localizedMessage)
                    )
                }
            }
        }
        return BaseResponse.Error(
            NetworkFailure()
        )
    }

}