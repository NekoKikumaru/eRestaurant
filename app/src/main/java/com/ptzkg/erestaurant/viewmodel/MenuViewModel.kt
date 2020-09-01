package com.ptzkg.erestaurant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ptzkg.erestaurant.model.ApiResponse
import com.ptzkg.erestaurant.model.Menu
import com.ptzkg.erestaurant.model.Menus
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MenuViewModel: BaseViewModel() {
    val MEDIA_TEXT = MediaType.parse("text/plain")
    val MEDIA_IMAGE = MediaType.parse("image/*")
    var menus: MutableLiveData<List<Menu>> = MutableLiveData()
    fun getMenus(): LiveData<List<Menu>> = menus

    fun loadMenus() {
        loading.value = true
        loadError.value = false
        val call = api.getMenus()

        call.enqueue(object : Callback<Menus> {
            override fun onFailure(call: Call<Menus>, t: Throwable) {
                loading.value = false
                loadError.value = true
            }

            override fun onResponse(call: Call<Menus>, response: Response<Menus>) {
                response.isSuccessful.let {
                    loading.value = false
                    menus.value = response.body()?.menus
                }
            }
        })
    }

    fun addMenu(name: String, sub_category_id: Int, price: String, image: String) {
        message.value = ""

        var name = RequestBody.create(MEDIA_TEXT, name)
        var sub_category_id = RequestBody.create(MEDIA_TEXT, sub_category_id.toString())
        var price = RequestBody.create(MEDIA_TEXT, price)

        val file = File(image)
        val requestBody = RequestBody.create(MEDIA_IMAGE, file)
        val image = MultipartBody.Part.createFormData("image",file.name,requestBody)

        val call = api.addMenu(name, sub_category_id, price, image)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                message.value = "Check your network connection"
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                message.value = response.body()!!.message
            }

        })
    }

    fun updateMenu(id: Int, name: String, sub_category_id: Int, price: String, image: String) {
        message.value = ""

        var name = RequestBody.create(MEDIA_TEXT, name)
        var sub_category_id = RequestBody.create(MEDIA_TEXT, sub_category_id.toString())
        var price = RequestBody.create(MEDIA_TEXT, price)

        val file = File(image)
        val requestBody = RequestBody.create(MEDIA_IMAGE, file)
        val image = MultipartBody.Part.createFormData("image",file.name,requestBody)

        val call = api.updateMenu(id, name, sub_category_id, price, image)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                message.value = "Check your network connection"
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                message.value = response.body()?.message
            }

        })
    }

    fun deleteMenu(id: Int) {
        message.value = ""
        val call = api.deleteMenu(id)

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