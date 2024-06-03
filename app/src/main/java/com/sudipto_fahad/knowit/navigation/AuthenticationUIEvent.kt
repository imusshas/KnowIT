package com.sudipto_fahad.knowit.navigation

sealed interface AuthenticationUIEvent {
    data class PhoneNoChanged(val phoneNo: String): AuthenticationUIEvent
    data class OTPChanged(val otp: String): AuthenticationUIEvent

    data object SignUpButtonClicked: AuthenticationUIEvent
    data object VerifyOTPButtonClicked: AuthenticationUIEvent
}