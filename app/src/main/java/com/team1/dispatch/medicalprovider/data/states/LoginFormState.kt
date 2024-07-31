package com.sehawafya.patient.data.states

data class LoginFormState(
    val isDataValid: Boolean = false,
    val isPhoneValid: Boolean = true,
    val isPasswordValid: Boolean = true
)