package com.ptzkg.erestaurant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ptzkg.erestaurant.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel: BaseViewModel() {
    var users: MutableLiveData<List<User>> = MutableLiveData()
    fun getUsers(): LiveData<List<User>> = users

    fun loadUsers() {
        loading.value = true
        loadError.value = false
        val call = api.getUsers()

        call.enqueue(object : Callback<Users> {
            override fun onFailure(call: Call<Users>, t: Throwable) {
                loading.value = false
                loadError.value = true
            }

            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                response.isSuccessful.let {
                    loading.value = false
                    users.value = response.body()?.users
                }
            }
        })
    }

    fun addUser(name: String, email: String, password: String, role: String) {
        message.value = ""
        val call = api.addUser(name, email, password, role)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                message.value = "Check your network connection"
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                message.value = response.body()!!.message
            }
        })
    }

    fun deleteUser(id: Int) {
        message.value = ""
        val call = api.deleteUser(id)

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