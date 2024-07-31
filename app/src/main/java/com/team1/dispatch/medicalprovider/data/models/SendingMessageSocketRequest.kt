package com.team1.dispatch.medicalprovider.data.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SendingMessageSocketRequest(
    @SerializedName("unitId")
    @Expose
    val unitId: String,

    @SerializedName("carRequestId")
    @Expose
    val carRequestId: String,

    @SerializedName("sender")
    @Expose
    val senderName: String,

    @SerializedName("message")
    @Expose
    val textMessage: String? = null
) : Parcelable