package com.team1.dispatch.medicalprovider.viewModels

import androidx.lifecycle.ViewModel
import com.team1.dispatch.medicalprovider.repositories.repo_interface.AuthRepository
import com.team1.dispatch.medicalprovider.repositories.repo_interface.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: HomeRepository) : ViewModel() {


}