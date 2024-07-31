package com.team1.dispatch.medicalprovider.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.team1.dispatch.medicalprovider.data.models.JoinChatRoomRequest
import com.team1.dispatch.medicalprovider.data.models.MessageModel
import com.team1.dispatch.medicalprovider.data.models.SendingMessageSocketRequest
import com.team1.dispatch.medicalprovider.data.models.SocketAcknowledgementModel
import com.team1.dispatch.medicalprovider.utils.Response
import com.team1.dispatch.medicalprovider.utils.ResponseType
import com.team1.dispatch.medicalprovider.utils.SessionManager
import io.socket.client.Ack
import io.socket.client.IO
import org.json.JSONObject
import javax.inject.Inject

class SocketIOManager @Inject constructor(val sessionManager: SessionManager) {
    private val TAG = "SocketIOInterface"

    private val socketOptions = IO.Options().apply {
        extraHeaders = mutableMapOf(
            "unitId" to listOf(
//                "${UserUtil.getUserUnitID()}"
            )
        )
    }

    private val socketIO = IO.socket(SOCKET_URL, socketOptions)

    val isConnected: Boolean
        get() = socketIO.connected()

    init {
        Log.d(TAG, "init SocketIOManager: ")
        connect()
    }

    fun connect() {
        socketIO.connect()
    }


    fun joinChatRoom(
        unitId: String,
        carRequestId: String,
        callback: (DataState<SocketAcknowledgementModel>) -> Unit
    ) {
        Log.d(TAG, "joinChatRoom: - IsSocketConnected $isConnected ")
        val request = JoinChatRoomRequest(unitId, carRequestId)
        val requestJsonObject = JSONObject(Gson().toJson(request))
        logRequest(requestJsonObject, JOIN_CHAT_ROOM)
        socketIO.emit(JOIN_CHAT_ROOM, requestJsonObject, Ack {
            Log.d(TAG, "joinChatRoom: ")
            callback(handelDataReceived(it, JOIN_CHAT_ROOM))
        })
    }

    fun fetchMessages(callback: (DataState<List<MessageModel>>) -> Unit) {
        Log.d(TAG, "fetchMessages: - IsSocketConnected $isConnected ")
        socketIO.on(CHAT_MESSAGES) {
            callback(handelDataReceived(it, CHAT_MESSAGES))
        }
    }

    fun sendMessage(
        unitId: String,
        carRequestId: String,
        textMessage: String? = null,
        callback: (DataState<MessageModel>) -> Unit
    ) {
        Log.d(TAG, "sendMessage: - IsSocketConnected -  ${isConnected}")
        val request = SendingMessageSocketRequest(
            unitId = unitId,
            carRequestId = carRequestId,
            senderName = sessionManager.getUserName(),
            textMessage = textMessage
        )
        val requestJsonObject = JSONObject(Gson().toJson(request))
        logRequest(requestJsonObject, SEND_MESSAGE_EVENT)
        socketIO.emit(SEND_MESSAGE_EVENT, requestJsonObject, Ack {
            Log.d(TAG, "sendMessage: sent")
            callback(handelDataReceived(it, SEND_MESSAGE_EVENT))
        })
    }


    fun onMessageReceived(callback: (DataState<MessageModel>) -> Unit)/*: LiveData<DataState<MessageModel>>*/ {
        socketIO.on(ON_MESSAGE_RECEIVE_EVENT) {
            callback(handelDataReceived(it, ON_MESSAGE_RECEIVE_EVENT))
        }
    }


    fun disconnect() = socketIO.disconnect()


    private inline fun <reified T> handelDataReceived(
        response: Array<Any>, eventName: String
    ): DataState<T> {
        Log.d(TAG, "handelDataReceived: $eventName -> $response")
        if (response.isNotEmpty()) {
            logResponse(response, eventName)
            try {
                val data = response[0].toString()
                val result = if (T::class == List::class) {
                    val listType = object : TypeToken<List<MessageModel>>() {}.type
                    Gson().fromJson(data, listType)
                } else {
                    Gson().fromJson(data, T::class.java)
                }
                if (result is SocketAcknowledgementModel) {
                    if (result.success == true) {
                        return DataState.success(
                            data = result, response = Response(
                                message = "Success", responseType = ResponseType.Toast()
                            )
                        )
                    } else {
                        Log.e(TAG, "$eventName: $FAILURE_ACKNOWLEDGEMENT -> ${result.message}")
                        return DataState.error(
                            response = Response(
                                message = result.message, responseType = ResponseType.Dialog()
                            )
                        )
                    }
                } else {
                    return DataState.success(
                        data = result, response = Response(
                            message = "Success", responseType = ResponseType.None()
                        )
                    )
                }
            } catch (e: JsonSyntaxException) {
                Log.e(TAG, "$eventName: ErrorResponse -> ${e.message}")
                e.printStackTrace()
                val result = SocketAcknowledgementModel(
                    success = false, message = "Error: ${e.message}"
                )
                return DataState.error(
                    response = Response(
                        message = result.message, responseType = ResponseType.Dialog()
                    )
                )
            }
        } else {
            Log.e(TAG, "$eventName: ErrorResponse -> No Data Received")
            return DataState.error(
                response = Response(
                    message = NO_DATA_RECEIVED, responseType = ResponseType.Toast()
                )
            )
        }
    }

    private fun logResponse(response: Array<Any>, eventName: String) {
        response.forEach {
            Log.d(TAG, "logResponse: $eventName -> $it")
        }
    }

    private fun logRequest(requestObject: JSONObject, eventName: String) {
        Log.d(TAG, "SocketRequest: $eventName -> $requestObject")
    }

    companion object {

        private const val SOCKET_URL = "https://dispatch-socket-app.habib.cloud"

        private const val SEND_MESSAGE_EVENT = "sendMessage"

        private const val JOIN_CHAT_ROOM = "join"

        private const val CHAT_MESSAGES = "messages"

        private const val ON_MESSAGE_RECEIVE_EVENT = "sendMessage"

        const val FAILURE_ACKNOWLEDGEMENT = "Failure Acknowledgement"
        const val NO_DATA_RECEIVED = "No Data Received"
    }
}