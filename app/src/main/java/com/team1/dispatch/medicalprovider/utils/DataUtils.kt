package com.team1.dispatch.medicalprovider.utils

import com.team1.dispatch.medicalprovider.data.models.CarRequestModel

class DataUtils {
    companion object {
        fun getSampleCarRequests(): List<CarRequestModel> {
            return listOf(
                CarRequestModel(
                    id = 1,
                    serviceType = "Transport",
                    patientName = "John Doe",
                    createdAt = "2023-10-01T10:00:00.000Z",
                    startDate = "2023-10-01"
                ),
                CarRequestModel(
                    id = 2,
                    serviceType = "Emergency",
                    patientName = "Jane Smith",
                    createdAt = "2023-10-02T11:00:00.000Z",
                    startDate = "2023-10-02"
                ),
                CarRequestModel(
                    id = 3,
                    serviceType = "Routine Check",
                    patientName = "Alice Johnson",
                    createdAt = "2023-10-03T12:00:00.000Z",
                    startDate = "2023-10-03"
                ),
                CarRequestModel(
                    id = 4,
                    serviceType = "Transport",
                    patientName = "Bob Brown",
                    createdAt = "2023-10-04T13:00:00.000Z",
                    startDate = "2023-10-04"
                ),
                CarRequestModel(
                    id = 5,
                    serviceType = "Emergency",
                    patientName = "Charlie Davis",
                    createdAt = "2023-10-05T14:00:00.000Z",
                    startDate = "2023-10-05"
                )
            )
        }
    }
}