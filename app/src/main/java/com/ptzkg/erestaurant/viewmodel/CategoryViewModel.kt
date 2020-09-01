package com.ptzkg.erestaurant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ptzkg.erestaurant.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel: BaseViewModel() {
    var categories: MutableLiveData<List<Category>> = MutableLiveData()
    fun getCategories(): LiveData<List<Category>> = categories

    fun loadCategories() {
        loading.value = true
        loadError.value = false
        val call = api.getCategories()

        call.enqueue(object : Callback<Categories> {
            override fun onFailure(call: Call<Categories>, t: Throwable) {
                loading.value = false
                loadError.value = true
            }

            override fun onResponse(call: Call<Categories>, response: Response<Categories>) {
                response.isSuccessful.let {
                    loading.value = false
                    categories.value = response.body()?.categories
                }
            }
        })
    }

    fun addCategory(name: String) {
        message.value = ""
        val call = api.addCategory(name)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                message.value = "Check your network connection"
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                message.value = response.body()!!.message
            }

        })
    }

    fun updateCategory(id: Int, name: String) {
        message.value = ""
        val call = api.updateCategory(id, name)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                message.value = "Check your network connection"
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                message.value = response.body()?.message
            }

        })
    }

    fun deleteCategory(id: Int) {
        message.value = ""
        val call = api.deleteCategory(id)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                message.value = "Check your network connection"
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                message.value = response.body()!!.message
            }
        })
    }
}