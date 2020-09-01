package com.ptzkg.erestaurant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ptzkg.erestaurant.model.*
import com.ptzkg.erestaurant.viewmodel.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubCategoryViewModel: BaseViewModel() {
    var subcategories: MutableLiveData<List<SubCategory>> = MutableLiveData()
    fun getSubCategories(): LiveData<List<SubCategory>> = subcategories

    fun loadSubCategories() {
        loading.value = true
        loadError.value = false
        val call = api.getSubCategories()

        call.enqueue(object : Callback<SubCategories> {
            override fun onFailure(call: Call<SubCategories>, t: Throwable) {
                loading.value = false
                loadError.value = true
            }

            override fun onResponse(call: Call<SubCategories>, response: Response<SubCategories>) {
                loading.value = false
                subcategories.value = response.body()?.subCategories
            }
        })
    }

    fun addSubCategory(name: String, category_id: Int) {
        message.value = ""
        val call = api.addSubCategory(name, category_id)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                message.value = "Check your network connection"
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                message.value = response.body()!!.message
            }

        })
    }

    fun updateSubCategory(id: Int, name: String, category_id: Int) {
        message.value = ""
        val call = api.updateSubCategory(id, name, category_id)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                message.value = "Check your network connection"
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                message.value = response.body()?.message
            }

        })
    }

    fun deleteSubCategory(id: Int) {
        message.value = ""
        val call = api.deleteSubCategory(id)

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