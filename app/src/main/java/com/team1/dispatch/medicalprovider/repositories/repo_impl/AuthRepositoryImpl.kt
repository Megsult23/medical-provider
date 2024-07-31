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
import kotlinx.coroutines.Job
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    val apiInterface: ApiInterface,
    val sessionManager: SessionManager
) : JobManager("AuthRepository"), AuthRepository {
    override fun login(phone: String, password: String): LiveData<DataState<UserModel>> {
        return object : NetworkBoundResource<GenericResponse<UserModel>, UserModel>(
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            isNetworkRequest = true,
            shouldLoadFromCache = false,
            shouldCancelIfNoInternet = true
        ) {
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<GenericResponse<UserModel>>) {
                if (response.body.success) {
                    //response.body.data?.let { sessionManager.saveUser(it) }
                    onCompleteJob(
                        DataState.success(
                            data = response.body.data,
                            response = Response(
                                message = response.body.message,
                                responseType = ResponseType.None()
                            ),
                            metadata = response.body.metaData
                        )
                    )
                } else {
                    onCompleteJob(
                        DataState.error(
                            response = Response(
                                message = response.body.message,
                                responseType = ResponseType.Dialog()
                            )
                        )
                    )
                }
            }

            override suspend fun createCacheRequest() {

            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse<UserModel>>> {
                return apiInterface.login(phone = phone, password = password)
            }

            override fun setJob(job: Job) {
                addJop("login", job)
            }

            override fun loadFromCache(): LiveData<UserModel> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: UserModel?) {

            }

        }.asLiveData()
    }

}