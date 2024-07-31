package com.team1.dispatch.medicalprovider.network

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class GenericResponse<T>(
    @SerializedName("success")
    @Expose
    var success: Boolean,

    @SerializedName("message")
    @Expose
    var message: String,

    @SerializedName("data")
    @Expose
    var data: T?,

    @SerializedName("meta")
    @Expose
    val metaData: MetaData?
)

@Parcelize
data class MetaData(
    @SerializedName("current_page")
    @Expose
    val currentPage: Int,

    @SerializedName("from")
    @Expose
    val from: Int,

    @SerializedName("last_page")
    @Expose
    val lastPage: Int
): Parcelable