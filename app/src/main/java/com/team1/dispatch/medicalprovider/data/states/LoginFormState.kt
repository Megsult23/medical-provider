package com.sehawafya.patient.data.states

data class LoginFormState(
    val isDataValid: Boolean = false,
    val isUserNameValid: Boolean = true,
    val isPasswordValid: Boolean = true
)