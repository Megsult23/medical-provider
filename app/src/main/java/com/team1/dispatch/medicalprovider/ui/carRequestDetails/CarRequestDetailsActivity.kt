package com.team1.dispatch.medicalprovider.ui.carRequestDetails

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.team1.dispatch.medicalprovider.R
import com.team1.dispatch.medicalprovider.base.BaseActivity
import com.team1.dispatch.medicalprovider.databinding.ActivityCarRequestDetailsBinding
import com.team1.dispatch.medicalprovider.ui.contactParamedic.ContactParamedicActivity
import com.team1.dispatch.medicalprovider.utils.Constants.Companion.CAR_REQUEST_ID_KEY
import com.team1.dispatch.medicalprovider.utils.displayToast
import com.team1.dispatch.medicalprovider.utils.setUpToolBar

class CarRequestDetailsActivity : BaseActivity() {
    private val TAG = "CarRequestDetailsActivi"

    private lateinit var binding: ActivityCarRequestDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarRequestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        setUpToolBar(
            toolbarBinding = binding.mainToolbar,
            isHome = false,
            title = getString(R.string.request_8345)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.car_request_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.action_medical_report -> {
                hideSoftKeyboard()
                //todo
                displayToast("Start Downloading")
                true
            }

            R.id.action_contact_paramedic -> {
                hideSoftKeyboard()
                startActivity(
                    Intent(
                        this@CarRequestDetailsActivity,
                        ContactParamedicActivity::class.java
                    ).putExtra(
                        CAR_REQUEST_ID_KEY,
                        intent.extras?.getString(CAR_REQUEST_ID_KEY)?.trim() ?: ""
                    )
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showProgressBar(show: Boolean) {

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


}