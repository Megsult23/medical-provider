package com.team1.dispatch.medicalprovider.network

import com.team1.dispatch.medicalprovider.utils.Data
import com.team1.dispatch.medicalprovider.utils.Event
import com.team1.dispatch.medicalprovider.utils.Loading
import com.team1.dispatch.medicalprovider.utils.Response
import com.team1.dispatch.medicalprovider.utils.StateError

data class DataState<T>(
    var error: Event<StateError>? = null,
    var loading: Loading = Loading(false),
    var data: Data<T>? = null,
    var metadata: MetaData? = null,
    var statusCode: Int? = null
) {
    companion object {
        fun <T> error(response: Response, statusCode: Int? = null): DataState<T> {
            return DataState(
                error = Event(StateError(response)),
                Loading(false),
                statusCode = statusCode
            )
        }

        fun <T> loading(isLoading: Boolean, cashedData: T? = null): DataState<T> {
            return DataState(
                loading = Loading(isLoading),
                data = Data(Event.dataEvent(cashedData), null)
            )
        }

        fun <T> success(
            data: T? = null,
            response: Response? = null,
            metadata: MetaData? = null
        ): DataState<T> {
            return DataState(
                data = Data(
                    Event.dataEvent(data), Event.responseEvent(response)
                ),
                metadata = metadata
            )
        }
    }
}