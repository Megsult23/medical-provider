package com.team1.dispatch.medicalprovider.utils

import com.team1.dispatch.medicalprovider.BuildConfig


class PreferenceKeys {
    companion object {
        const val USER_EMAIL = "${BuildConfig.APPLICATION_ID}.USER_EMAIL"
        const val USER_PHONE = "${BuildConfig.APPLICATION_ID}.USER_PHONE"
        const val USER_NAME = "${BuildConfig.APPLICATION_ID}.USER_NAME"
        const val USER_ID = "${BuildConfig.APPLICATION_ID}.USER_ID"
        const val APP_PREFERENCES = "${BuildConfig.APPLICATION_ID}.APP_PREFERENCES"
        const val USER_LANGUAGE = "${BuildConfig.APPLICATION_ID}.USER_LANGUAGE"
        const val FIRST_TIME = "${BuildConfig.APPLICATION_ID}.FIRST_TIME"
        const val TOKEN = "${BuildConfig.APPLICATION_ID}.TOKEN"
        const val USER_NIGHT_MODE = "${BuildConfig.APPLICATION_ID}.USER_NIGHT_MODE"
        const val USER_IMAGE = "${BuildConfig.APPLICATION_ID}.USER_IMAGE"
        const val DEVICE_TOKEN = "${BuildConfig.APPLICATION_ID}.DEVICE_TOKEN"
    }
}