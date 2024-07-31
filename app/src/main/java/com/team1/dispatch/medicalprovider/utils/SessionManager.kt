package com.team1.dispatch.medicalprovider.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatDelegate
import com.team1.dispatch.medicalprovider.data.models.UserModel
import com.team1.dispatch.medicalprovider.utils.Constants.Companion.ARABIC
import com.team1.dispatch.medicalprovider.utils.PreferenceKeys.Companion.FIRST_TIME
import com.team1.dispatch.medicalprovider.utils.PreferenceKeys.Companion.USER_LANGUAGE
import javax.inject.Inject

class SessionManager @Inject constructor(
    val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    interface LogoutCallback {
        fun onLogoutComplete()
    }

    private var logoutCallback: LogoutCallback? = null

    fun setLogoutCallback(callback: LogoutCallback) {
        logoutCallback = callback
    }

    fun logout() {
        val currentLanguage = getLanguage()
        sharedPreferences.edit().clear().apply()
        setFirstTime(false)
        setLanguage(currentLanguage)
        logoutCallback?.onLogoutComplete()
    }

    fun isInternetAvailable(): Boolean {
        val result: Boolean
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return result
    }

    fun getLanguage(): String = sharedPreferences.getString(USER_LANGUAGE, ARABIC) ?: ARABIC
    fun setLanguage(language: String) =
        sharedPreferences.edit().putString(USER_LANGUAGE, language).apply()

    fun isFirstTime(): Boolean = sharedPreferences.getBoolean(FIRST_TIME, true)
    fun setFirstTime(firstTime: Boolean) =
        sharedPreferences.edit().putBoolean(FIRST_TIME, firstTime).apply()

    fun setToken(token: String) =
        sharedPreferences.edit().putString(PreferenceKeys.TOKEN, token).apply()

    fun getToken(): String = sharedPreferences.getString(PreferenceKeys.TOKEN, "") ?: ""

    fun getNightMode(): Int {
        return sharedPreferences.getInt(
            PreferenceKeys.USER_NIGHT_MODE,
            AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    fun saveUserModel(userModel: UserModel) {
        userModel.user?.let { saveUser(it) }
        sharedPreferences.edit().putString(PreferenceKeys.TOKEN, userModel.token).apply()
    }

    fun saveUser(user: UserModel.User) {
        sharedPreferences.edit().putString(PreferenceKeys.USER_ID, user.id).apply()
        sharedPreferences.edit().putString(PreferenceKeys.USER_NAME, user.name).apply()
        sharedPreferences.edit().putString(PreferenceKeys.USER_PHONE, user.phone).apply()
        sharedPreferences.edit().putString(PreferenceKeys.USER_EMAIL, user.email).apply()
        sharedPreferences.edit().putString(PreferenceKeys.USER_IMAGE, user.image).apply()
    }

    fun getUserImage(): String = sharedPreferences.getString(PreferenceKeys.USER_IMAGE, "") ?: ""

    fun getUserID(): String = sharedPreferences.getString(PreferenceKeys.USER_ID, "") ?: ""

    fun getUserPhone(): String = sharedPreferences.getString(PreferenceKeys.USER_PHONE, "") ?: ""

    fun getUserName(): String = sharedPreferences.getString(PreferenceKeys.USER_NAME, "") ?: ""

    fun getUserEmail(): String = sharedPreferences.getString(PreferenceKeys.USER_EMAIL, "") ?: ""

    fun setDeviceToken(token: String) =
        sharedPreferences.edit().putString(PreferenceKeys.DEVICE_TOKEN, token).apply()

    fun getDeviceToken(): String =
        sharedPreferences.getString(PreferenceKeys.DEVICE_TOKEN, "").orDefault()


    /* todo fcm   fun fetchToken() {
            FirebaseApp.initializeApp(context)
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                token?.let { setDeviceToken(it) }
            }
        }*/

}