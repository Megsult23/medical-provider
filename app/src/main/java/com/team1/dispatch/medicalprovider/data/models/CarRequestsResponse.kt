package com.team1.dispatch.medicalprovider.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class CarRequestsResponse(
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<CarRequestModel>? = null
)

@Parcelize
data class CarRequestModel(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("unit_id") var unitId: String? = null,
    @SerializedName("start_date") var startDate: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("location") var location: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("blood_type") var bloodType: String? = null,
    @SerializedName("date_of_birth") var dateOfBirth: String? = null,
    @SerializedName("nationality") var nationality: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("dispatcher_phone") var dispatcherPhone: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("address_latitude") var addressLatitude: String? = null,
    @SerializedName("address_longitude") var addressLongitude: String? = null,
    @SerializedName("destination_latitude") var destinationLatitude: String? = null,
    @SerializedName("destination_longitude") var destinationLongitude: String? = null,
    @SerializedName("patient_id") var patientId: String? = null,
    @SerializedName("patient_name") var patientName: String? = null,
    @SerializedName("type") var serviceType: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("medical_history") var medicalHistory: String? = null,
    @SerializedName("medical_report") var medicalReport: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("distance") var distance: String? = null,
    @SerializedName("service") var service: String? = null,
    @SerializedName("signatures") var signatures: ArrayList<SignatureModel>? = null,
    @SerializedName("activity_log") var carActivityLog: ArrayList<CarActivityLogData>? = null
): Parcelable

@Parcelize
data class CarActivityLogData(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    var value: Int
) : Parcelable