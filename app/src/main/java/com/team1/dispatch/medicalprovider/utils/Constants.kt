package com.team1.dispatch.medicalprovider.utils

class Constants {
    companion object {
        const val BASE_URL = "https://api-staging.sehawafeya.com/api/"
        const val SOCKET_URL = "https://api-staging.sehawafeya.com"
        const val UNABLE_TODO_OPERATION_WO_INTERNET = "No Internet"
        const val NETWORK_TIMEOUT = 60000L
        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing
        const val ERROR_UNKNOWN = "Unknown error"
        const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."
        const val IS_RESTART = "IsRestart"
        const val ARABIC = "ar"
        const val ENGLISH = "en"
        const val CAR_REQUEST_KEY = "car_request_key"
        const val CAR_REQUEST_ID_KEY = "car_request_id_key"
        const val VOICE_CALL = "voice"
        const val VIDEO_CALL = "video"
        const val INCOMING_CALL_DATA = "incoming_call_data"
        val EXPECTED_INTERNET_ERROR_MESSAGES =
            listOf(UNABLE_TODO_OPERATION_WO_INTERNET, UNABLE_TO_RESOLVE_HOST)
    }
}