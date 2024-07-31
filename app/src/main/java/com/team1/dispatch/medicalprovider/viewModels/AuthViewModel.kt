package com.team1.dispatch.medicalprovider.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sehawafya.patient.data.states.LoginFormState
import com.sehawafya.patient.utils.Validation
import com.team1.dispatch.medicalprovider.data.models.UserModel
import com.team1.dispatch.medicalprovider.repositories.repo_interface.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AuthRepository) : ViewModel() {

    var userModel: UserModel? = null

    val loginFormState = MutableLiveData<LoginFormState>()
    fun checkLoginFormValidation(isPhoneValid: Boolean, password: String) {
        if (!isPhoneValid) {
            loginFormState.value = LoginFormState(isPhoneValid = false)
        } else if (!Validation.isValidPassword(password)) {
            loginFormState.value = LoginFormState(isPasswordValid = false)
        } else {
            loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    fun login(phone: String, password: String) = repo.login(phone, password)

}