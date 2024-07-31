package com.team1.dispatch.medicalprovider.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.team1.dispatch.medicalprovider.R
import com.team1.dispatch.medicalprovider.base.BaseActivity
import com.team1.dispatch.medicalprovider.databinding.ActivityLoginBinding
import com.team1.dispatch.medicalprovider.ui.home.HomeActivity
import com.team1.dispatch.medicalprovider.utils.hide
import com.team1.dispatch.medicalprovider.utils.show
import com.team1.dispatch.medicalprovider.viewModels.AuthViewModel

class AuthActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }


    private fun initViews() = binding.apply {
        observeDataValidation()
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                checkDataValidation()
            }
        }
        etUsername.addTextChangedListener(textWatcher)
        etPassword.addTextChangedListener(textWatcher)
        etPassword.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Call the request function here
                if (btnLogin.isEnabled) startLoginProcess()
                true
            } else {
                false
            }
        }
        btnLogin.setOnClickListener(this@AuthActivity)
    }

    private fun observeDataValidation() {
        viewModel.loginFormState.observe(this) {
            it?.let {
                resetLoginFormErrors()
                binding.btnLogin.isEnabled = it.isDataValid
                if (!it.isDataValid) {
                    it.apply {
                        if (!isUserNameValid) {
                            binding.tilPhone.background = ContextCompat.getDrawable(
                                this@AuthActivity,
                                R.drawable.bg_white_r10_red_l_st
                            )
                            binding.tvUsernameError.show()
                        } else if (!isPasswordValid) {
                            binding.tilPassword.background = ContextCompat.getDrawable(
                                this@AuthActivity,
                                R.drawable.bg_white_r10_red_l_st
                            )
                            binding.tvPasswordError.show()
                        }
                    }
                }
            }
        }
    }

    private fun checkDataValidation() {
        viewModel.checkLoginFormValidation(
            userName = binding.etUsername.text.toString().trim(),
            password = binding.etPassword.text.toString().trim()
        )
    }

    private fun resetLoginFormErrors() = binding.apply {
        tilPhone.background =
            ContextCompat.getDrawable(this@AuthActivity, R.drawable.bg_white_r10_move_st)
        tilPassword.background =
            ContextCompat.getDrawable(this@AuthActivity, R.drawable.bg_white_r10_move_st)
        tvPasswordError.hide()
        tvUsernameError.hide()
    }

    private fun startLoginProcess() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        /*   todo     viewModel.login(
                    phone = binding.etUsername.text.toString().trim(),
                    password = binding.etPassword.text.toString().trim()
                ).observe(this) {
                    it.error?.getContentIfNotHandled()?.let { stateError ->
                        onErrorStateChange(stateError)
                    }

                    showProgressBar(it.loading.isLoading)

                    it.data?.data?.getContentIfNotHandled()?.let { userModel ->
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                }*/
    }

    override fun showProgressBar(show: Boolean) {
        if (show) {
            binding.btnLogin.hide()
            binding.pbLoading.show()
        } else {
            binding.pbLoading.hide()
            binding.btnLogin.show()
        }
    }

    override fun showErrorUI(
        show: Boolean,
        image: Int?,
        title: String?,
        desc: String?,
        isAuthenticated: Boolean?,
        showButton: Boolean?
    ) {
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                btnLogin -> {
                    hideSoftKeyboard()
                    startLoginProcess()
                }
            }
        }
    }
}