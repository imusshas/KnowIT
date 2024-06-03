package com.sudipto_fahad.knowit.data

import com.sudipto_fahad.knowit.model.api_response.RequestOTPResponse
import com.sudipto_fahad.knowit.model.api_response.VerifyOTPResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface BdAppsApiRepository {
    suspend fun requestOTP(subscriberId: String): Flow<Response<RequestOTPResponse>>

    suspend fun verifyOTP(
        referenceNo: String,
        otp: String
    ): Flow<Response<VerifyOTPResponse>>
}