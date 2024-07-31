package com.team1.dispatch.medicalprovider.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class LifecycleHandler(private val owner: LifecycleOwner) : Handler(Looper.getMainLooper())
{
    private val TAG = "LifecycleHandler"
    private var runnable: () -> Unit = {}

    private var isRepeated = false

    private var millisecondDelay: Long = 0

    fun doAction(delay: Long, r: () -> Unit)
    {
        removeCallbacksAndMessages(null)
        isRepeated = false
        runnable = r
        millisecondDelay = delay
        postDelayed(::executeAction, delay)
    }

    fun doRepeatedAction(delay: Long, r: () -> Unit)
    {
        removeCallbacksAndMessages(null)
        isRepeated= true
        runnable = r
        millisecondDelay = delay
        postDelayed(::executeAction, delay)
    }

    private fun executeAction()
    {
        if(owner.lifecycle.currentState == Lifecycle.State.DESTROYED)
        {
            Log.d(TAG, "executeAction: owner.lifecycle.currentState == Lifecycle.State.DESTROYED")
            removeCallbacksAndMessages(null)
            return
        }
        runnable()

        if(isRepeated)
        {
            postDelayed(::executeAction, millisecondDelay)
        }
    }
}