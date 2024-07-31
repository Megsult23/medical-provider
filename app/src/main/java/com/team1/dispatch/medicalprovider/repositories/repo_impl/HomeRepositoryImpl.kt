package com.team1.dispatch.medicalprovider.repositories.repo_impl

import androidx.lifecycle.LiveData
import com.team1.dispatch.medicalprovider.utils.Response
import com.team1.dispatch.medicalprovider.utils.ResponseType
import com.team1.dispatch.medicalprovider.utils.SessionManager
import com.team1.dispatch.medicalprovider.data.models.UserModel
import com.team1.dispatch.medicalprovider.network.AbsentLiveData
import com.team1.dispatch.medicalprovider.network.ApiInterface
import com.team1.dispatch.medicalprovider.network.ApiSuccessResponse
import com.team1.dispatch.medicalprovider.network.DataState
import com.team1.dispatch.medicalprovider.network.GenericApiResponse
import com.team1.dispatch.medicalprovider.network.GenericResponse
import com.team1.dispatch.medicalprovider.network.JobManager
import com.team1.dispatch.medicalprovider.network.NetworkBoundResource
import com.team1.dispatch.medicalprovider.repositories.repo_interface.AuthRepository
import com.team1.dispatch.medicalprovider.repositories.repo_interface.HomeRepository
import kotlinx.coroutines.Job
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    val apiInterface: ApiInterface,
    val sessionManager: SessionManager
) : JobManager("HomeRepository"), HomeRepository {


}