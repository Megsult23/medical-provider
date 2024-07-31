package com.team1.dispatch.medicalprovider.utils

import com.team1.dispatch.medicalprovider.network.DataState

interface DataStateChangeListener {
    fun onDataStateChange(dataState: DataState<*>?)
    fun hideSoftKeyboard()
    fun showSoftKeyboard()
    fun onErrorStateChange(stateError: StateError)
}