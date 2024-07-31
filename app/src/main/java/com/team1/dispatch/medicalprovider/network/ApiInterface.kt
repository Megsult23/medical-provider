package com.team1.dispatch.medicalprovider.network

import androidx.lifecycle.LiveData
import com.team1.dispatch.medicalprovider.data.models.UserModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @POST("auth/login")
    @FormUrlEncoded
    fun login(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): LiveData<GenericApiResponse<GenericResponse<UserModel>>>
}