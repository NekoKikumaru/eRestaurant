package com.ptzkg.erestaurant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ptzkg.erestaurant.api.eRestaurantApi

open class BaseViewModel(): ViewModel() {
    val api: eRestaurantApi = eRestaurantApi()

    var message = MutableLiveData<String>().apply { value = "" }
    fun getMessage(): LiveData<String> = message

    var loading = MutableLiveData<Boolean>().apply { value = true }
    fun getLoading(): LiveData<Boolean> = loading

    var loadError = MutableLiveData<Boolean>().apply { value = false }
    fun getLoadError(): LiveData<Boolean> = loadError
}