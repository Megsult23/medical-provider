package com.team1.dispatch.medicalprovider.repositories.repo_interface

import androidx.lifecycle.LiveData
import com.team1.dispatch.medicalprovider.data.models.UserModel
import com.team1.dispatch.medicalprovider.network.DataState

interface AuthRepository {
    fun login(phone: String, password: String): LiveData<DataState<UserModel>>

}