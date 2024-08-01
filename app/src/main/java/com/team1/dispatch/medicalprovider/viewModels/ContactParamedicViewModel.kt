package com.team1.dispatch.medicalprovider.viewModels

import androidx.lifecycle.ViewModel
import com.team1.dispatch.medicalprovider.repositories.repo_interface.ContactParamedicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactParamedicViewModel @Inject constructor(
    private val contactParamedicRepository: ContactParamedicRepository
) : ViewModel() {

}