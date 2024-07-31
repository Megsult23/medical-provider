package com.team1.dispatch.medicalprovider.data.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SocketAcknowledgementModel(
    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("success")
    @Expose
    val success: Boolean? = null,

    @SerializedName("_id")
    @Expose
    val chatID: String? = null
) : Parcelable