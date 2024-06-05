package com.sudipto_fahad.knowit.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudipto_fahad.knowit.data.BdAppsApiRepository
import com.sudipto_fahad.knowit.data.UserPref
import com.sudipto_fahad.knowit.model.UserModel
import com.sudipto_fahad.knowit.model.UserValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val userPref: UserPref,
    private val bdAppsApiRepository: BdAppsApiRepository
) : ViewModel() {

    val user = userPref.getUserModel().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        UserModel()
    )

    private val _phoneNo = MutableStateFlow("")
    val phoneNo = _phoneNo.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _otp = MutableStateFlow("")
    val otp = _otp.asStateFlow()

    private val _otpError = MutableStateFlow<String?>(null)
    val otpError = _otpError.asStateFlow()

    private val _navigateToOTPScreen = MutableStateFlow(false)
    val navigateToOTPScreen = _navigateToOTPScreen.asStateFlow()

    private val _navigateToPromptScreen = MutableStateFlow(false)
    val navigateToPromptScreen = _navigateToPromptScreen.asStateFlow()


    private val _referenceNo = MutableStateFlow<String?>(null)
    private val referenceNo = _referenceNo.asStateFlow()



    fun onEvent(event: AuthenticationUIEvent) {
        when (event) {
            is AuthenticationUIEvent.PhoneNoChanged -> {
                _phoneNo.update { event.phoneNo }
            }
            AuthenticationUIEvent.SignUpButtonClicked -> {
                if (validatePhoneNoState()) {
                    signIn()
                }
            }
            is AuthenticationUIEvent.OTPChanged -> {
                _otp.update { event.otp }
            }
            AuthenticationUIEvent.VerifyOTPButtonClicked -> {
                verifyOTP()
            }
        }
    }

    private fun saveUser(userModel: UserModel) = viewModelScope.launch(Dispatchers.IO) {
        userPref.saveUserModel(userModel)
        _navigateToPromptScreen.update { true }
        _phoneNo.update { "" }
    }


    private fun signIn() = viewModelScope.launch {

        val phone = phoneNo.value
        val subscriberId = if (phone[0] == '+' && phone[1] == '8' && phone[2] == '8') {
            phone.substring(3)
        } else if (phone[0] == '8' && phone[1] == '8') {
            phone.substring(2)
        } else {
            phone
        }

        bdAppsApiRepository.requestOTP(subscriberId = subscriberId).collectLatest {
            val response = it
            if (response.isSuccessful) {
                val requestOTPResponse = response.body()
                Log.d(TAG, "requestOTP: ${response.body()}")
                if (requestOTPResponse?.statusCode == "S1000") {
                    _referenceNo.update { requestOTPResponse.referenceNo }
                    _navigateToOTPScreen.update { true }
                } else if (requestOTPResponse?.statusDetail == "user already registered") {
                    _error.update { "User is Already Registered" }
                } else {
                    _error.update { "Something went wrong. Please try again." }
                }
            } else {
                Log.d(TAG, "requestOTP: error: ${response.errorBody()}")
                _error.update { "Something went wrong. Please try again." }
            }
        }
    }


    private fun verifyOTP() = viewModelScope.launch {
        referenceNo.value?.let { referenceNo ->
            bdAppsApiRepository.verifyOTP(
                referenceNo = referenceNo,
                otp = otp.value
            ).collectLatest {
                val verifyResponse = it
                if (verifyResponse.isSuccessful) {
                    val verifyOTPResponse = verifyResponse.body()
                    if (verifyOTPResponse?.statusCode == "S1000") {
                        saveUser(UserModel(phoneNo = phoneNo.value))
                    } else {
                        _otpError.update { "Invalid OTP" }
                    }
                } else {
                    Log.d(TAG, "verifyOTP: error: ${verifyResponse.errorBody()}")
                    _otpError.update { "Something went wrong. Please try again." }
                }
            }
        }
    }


    private fun validatePhoneNoState(): Boolean {
        _error.update {
            UserValidator.validatePhoneNo(phoneNo.value)
        }

        return error.value == null
    }

    companion object {
        private const val TAG = "NavigationViewModel"
    }
}