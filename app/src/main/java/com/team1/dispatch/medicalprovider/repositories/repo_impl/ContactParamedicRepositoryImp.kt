package com.team1.dispatch.medicalprovider.repositories.repo_impl

import com.team1.dispatch.medicalprovider.network.ApiInterface
import com.team1.dispatch.medicalprovider.network.SocketIOManager
import com.team1.dispatch.medicalprovider.repositories.repo_interface.ContactParamedicRepository
import com.team1.dispatch.medicalprovider.utils.SessionManager
import javax.inject.Inject

class ContactParamedicRepositoryImp @Inject constructor(
    val apiInterface: ApiInterface,
    val sessionManager: SessionManager,
    val socketIOManager: SocketIOManager
) : ContactParamedicRepository {
}