package com.team1.dispatch.medicalprovider.data.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    @SerializedName("authToken")
    @Expose
    val token: String? = null,

    @SerializedName("passwordResetToken")
    @Expose
    val passwordResetToken: String? = null,

    @SerializedName("user")
    @Expose
    val user: User? = null

): Parcelable
{
    @Parcelize
    data class User(
        @SerializedName("_id")
        @Expose
        val id: String? = null,

        @SerializedName("name")
        @Expose
        val name: String? = null,

        @SerializedName("phone")
        @Expose
        val phone: String? = null,

        @SerializedName("image")
        @Expose
        val image: String? = null,

        @SerializedName("email")
        @Expose
        val email: String? = null,

        @SerializedName("verified")
        @Expose
        val verified: Boolean? = null,

        @SerializedName("dateOfBirth")
        @Expose
        val dateOfBirth: String? = null,

        @SerializedName("registeredBy")
        @Expose
        val registeredBy: String? = null,


        @SerializedName("createdAt")
        @Expose
        val createdAt: String? = null,

        @SerializedName("updatedAt")
        @Expose
        val updatedAt: String? = null,

        @SerializedName("unReadNotifications")
        @Expose
        val notificationCount: Int? = null,

        @SerializedName("__v")
        @Expose
        val v: Int? = null
    ): Parcelable

}