package com.mamsky.pcsprofile.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mamsky.pcsprofile.core.handle
import com.mamsky.pcsprofile.domain.di.RemoteSource
import com.mamsky.pcsprofile.domain.model.ProfileModel
import com.mamsky.pcsprofile.domain.usecase.SimpleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileListViewModel @Inject constructor(
    @RemoteSource private val useCase: SimpleUseCase
): ViewModel() {

    private val _viewState = MutableStateFlow<ProfileScreenState>(ProfileScreenState.OnLoading("Init..."))
    val viewState = _viewState.asStateFlow()

    fun test() {
        viewModelScope.launch {
            log("launch")
            useCase.getList().handle {
                onSuccess { data ->
                    log("onSuccess ${data.size} - ${data.map { it.id }}")
                    _viewState.value = ProfileScreenState.OnSuccess(data)
                }
                onFailure {
                    log("onFailure ${it.message}")
                    _viewState.value = ProfileScreenState.OnError(it.message ?: "Unknown Error")
                }
                onLoading {
                    log("onLoading")
                    _viewState.value = ProfileScreenState.OnLoading("On fetching...")
                }
            }
        }
    }

    private fun log(m: String) {
        println("ProfileScreen: VM - $m ")
    }

}

sealed class ProfileScreenState(val data: Any) {
    data class OnSuccess(val list: List<ProfileModel>): ProfileScreenState(list)
    data class OnError(val message: String): ProfileScreenState(message)
    data class OnLoading(val message: String): ProfileScreenState(message)
}