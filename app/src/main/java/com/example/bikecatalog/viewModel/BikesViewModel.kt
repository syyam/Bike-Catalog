package com.example.bikecatalog.viewModel

import android.bluetooth.BluetoothHidDevice
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.bikecatalog.model.Bikes
import com.example.bikecatalog.repos.BikesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BikesViewModel @Inject constructor(
    private val repository: BikesRepository
) : ViewModel() {

    val bikeList = MutableLiveData<List<Bikes>>()
    val progressBarStatus = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()


    fun fetchBikes() {
        progressBarStatus.value = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.getSearchResults()

                if (response.isSuccessful) {
                    bikeList.postValue(response.body()?.items)
                    progressBarStatus.postValue(false)

                } else {
                    progressBarStatus.postValue(false)
                    error.postValue(response.message())
                }
            } catch (e: Exception) {
                error.postValue(ERROR_MESSAGE)
                progressBarStatus.postValue(false)

            }

        }


    }

    companion object {
        private const val ERROR_MESSAGE = "There was an error connecting to the internet"

    }

}