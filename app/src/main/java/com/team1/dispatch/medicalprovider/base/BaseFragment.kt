package com.team1.dispatch.medicalprovider.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bumptech.glide.RequestManager
import com.team1.dispatch.medicalprovider.utils.MainUtils
import com.team1.dispatch.medicalprovider.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment : Fragment()
{
    private val TAG = "BaseFragment"

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var requestManager: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUserSettings()
    }

    override fun onStart() {
        super.onStart()
        initUserSettings()
    }

    private fun initUserSettings() {
        MainUtils.initializeSelectedLanguage(
            requireContext(), sessionManager.getLanguage()
        )
    }
    abstract fun showProgressBar(show: Boolean)

    abstract fun showErrorUI(
        show: Boolean,
        image: Int? = 0,
        title: String? = "",
        desc: String? = null,
        isAuthenticated: Boolean? = true,
        showButton: Boolean? = false
    )
}