package com.team1.dispatch.medicalprovider.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.team1.dispatch.medicalprovider.R
import com.team1.dispatch.medicalprovider.adapters.CarRequestsAdapter
import com.team1.dispatch.medicalprovider.base.BaseActivity
import com.team1.dispatch.medicalprovider.databinding.ActivityHomeBinding
import com.team1.dispatch.medicalprovider.utils.DataUtils
import com.team1.dispatch.medicalprovider.utils.setUpToolBar
import com.team1.dispatch.medicalprovider.viewModels.HomeViewModel

class HomeActivity : BaseActivity() {
    private val TAG = "HomeActivity"
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private var carRequestsAdapter: CarRequestsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        setUpToolBar(
            toolbarBinding = binding.mainToolbar,
            isHome = true
        )

        carRequestsAdapter = CarRequestsAdapter()
        binding.rvCarRequests.apply {
            carRequestsAdapter?.submitList(DataUtils.getSampleCarRequests())
            adapter = carRequestsAdapter
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.action_logout -> {
                hideSoftKeyboard()
                //todo
                true
            }

            R.id.action_language -> {
                hideSoftKeyboard()
                //todo
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