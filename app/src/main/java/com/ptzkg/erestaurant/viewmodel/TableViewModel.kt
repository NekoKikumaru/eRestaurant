package com.ptzkg.erestaurant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ptzkg.erestaurant.model.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TableViewModel: BaseViewModel() {
    var tables: MutableLiveData<List<Table>> = MutableLiveData()
    fun getTables(): LiveData<List<Table>> = tables

    fun loadTables() {
        loading.value = true
        loadError.value = false
        val call = api.getTables()

        call.enqueue(object : Callback<Tables> {
            override fun onFailure(call: Call<Tables>, t: Throwable) {
                loading.value = false
                loadError.value = true
            }

            override fun onResponse(call: Call<Tables>, response: Response<Tables>) {
                response.isSuccessful.let {
                    loading.value = false
                    tables.value = response.body()?.tables
                }
            }
        })
    }
    
    fun addTable(table_no: String) {
        message.value = ""
        val call = api.addTable(table_no)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                message.value = "Check your network connection"
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                message.value = response.body()!!.message
            }
        })
    }

    fun updateTable(id: Int, table_no: String) {
        message.value = ""
        val call = api.updateTable(id, table_no)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                message.value = "Check your network connection"
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                message.value = response.body()?.message
            }

        })
    }

    fun deleteTable(id: Int) {
        message.value = ""
        val call = api.deleteTable(id)

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