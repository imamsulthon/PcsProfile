package com.mamsky.pcsprofile.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mamsky.pcsprofile.core.handle
import com.mamsky.pcsprofile.domain.di.RemoteSource
import com.mamsky.pcsprofile.domain.model.ProfileModel
import com.mamsky.pcsprofile.domain.usecase.SimpleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    @RemoteSource private val useCase: SimpleUseCase
): ViewModel() {

    private val _data = MutableStateFlow(ProfileModel("", "", "", "", "", "", "", "", "", ""))
    val data = _data.asStateFlow()

    fun getDetail(id: String) {
        viewModelScope.launch {
            useCase.getDetail(id).handle {
                onSuccess { d ->
                    _data.update { d }
                }
            }
        }
    }
}