package com.team1.dispatch.medicalprovider.data.models

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignatureModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("role") var role: String? = null,
    @SerializedName("signature") var signature: String? = null,
    var signatureBitmap: Bitmap? = null
) : Parcelable


data class PostSignatureModel(
    @SerializedName("signatures") var signatures: ArrayList<SignatureData>? = null
)

data class SignatureData(
    @SerializedName("car_request_id") var carRequestId: String?,
    @SerializedName("name") var name: String? = null,
    @SerializedName("role") var role: String? = null,
    @SerializedName("signature") var signature: String? = null
)



