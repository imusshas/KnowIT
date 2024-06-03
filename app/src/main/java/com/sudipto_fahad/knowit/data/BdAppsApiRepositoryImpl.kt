package com.sudipto_fahad.knowit.data

import android.util.Log
import com.sudipto_fahad.knowit.api.BdAppsApi
import com.sudipto_fahad.knowit.model.api_response.RequestOTPResponse
import com.sudipto_fahad.knowit.model.api_response.VerifyOTPResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class BdAppsApiRepositoryImpl @Inject constructor(
    private val api: BdAppsApi
) : BdAppsApiRepository {
    override suspend fun requestOTP(subscriberId: String): Flow<Response<RequestOTPResponse>> =
        flow {
            emit(api.requestOTP(subscriberId))
        }.catch {
            Log.d(TAG, "requestOTP: ${it.message}")
        }

    override suspend fun verifyOTP(
        referenceNo: String,
        otp: String
    ): Flow<Response<VerifyOTPResponse>> = flow {
        emit(api.verifyOTP(referenceNo, otp))
    }.catch {
        Log.d(TAG, "verifyOTP: ${it.message}")
    }


    companion object {
        private const val TAG = "BdAppsApiRepositoryImpl"
    }

}