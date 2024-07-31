package com.team1.dispatch.medicalprovider.data.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class MessageModel(
    @SerializedName("_id")
    @Expose
    val id: String? = null,

    @SerializedName("message")
    @Expose
    val text: String? = null,

    @SerializedName("sender")
    @Expose
    val senderUnitName: String? = null,

    @SerializedName("user")
    @Expose
    val userId: String? = null,

    @SerializedName("createdAt")
    @Expose
    val createdAt: String? = null
) : Parcelable {


    fun isSendingMessage(loggedInUserId: String): Boolean {
        return loggedInUserId == userId
    }

}