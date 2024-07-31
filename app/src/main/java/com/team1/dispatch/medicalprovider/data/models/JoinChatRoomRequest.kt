package com.team1.dispatch.medicalprovider.data.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class JoinChatRoomRequest(
    @SerializedName("unitId")
    @Expose
    val unitId: String,
    @SerializedName("carRequestId")
    @Expose
    val carRequestId: String
) : Parcelable