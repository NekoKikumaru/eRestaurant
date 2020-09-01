package com.ptzkg.erestaurant.api

import com.ptzkg.erestaurant.model.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class eRestaurantApi {
    private lateinit var apiInterface: eRestaurantApiInterface

    companion object {
        const val BASE_URL = "http://menu-order.khaingthinkyi.me/api/setup/"
    }

    init {
        val okHttpClient = OkHttpClient.Builder().build()
        // val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        apiInterface = retrofit.create(eRestaurantApiInterface::class.java)
    }

    fun getCategories(): Call<Categories> {
        return apiInterface.getCategories()
    }

    fun addCategory(name: String): Call<ApiResponse> {
        return apiInterface.addCategory(name)
    }

    fun updateCategory(id: Int, name: String): Call<ApiResponse> {
        return apiInterface.updateCategory(id, name)
    }

    fun deleteCategory(id: Int): Call<ApiResponse> {
        return apiInterface.deleteCategory(id)
    }

    fun getSubCategories(): Call<SubCategories> {
        return apiInterface.getSubCategories()
    }

    fun addSubCategory(name: String, category_id: Int): Call<ApiResponse> {
        return apiInterface.addSubCategory(name, category_id)
    }

    fun updateSubCategory(id: Int, name: String, category_id: Int): Call<ApiResponse> {
        return apiInterface.updateSubCategory(id, name, category_id)
    }

    fun deleteSubCategory(id: Int): Call<ApiResponse> {
        return apiInterface.deleteSubCategory(id)
    }

    fun getMenus(): Call<Menus> {
        return apiInterface.getMenus()
    }

    fun addMenu(name: RequestBody, sub_category_id: RequestBody, price: RequestBody, image: MultipartBody.Part): Call<ApiResponse> {
        return apiInterface.addMenu(name, sub_category_id, price, image)
    }

    fun updateMenu(id: Int, name: RequestBody, sub_category_id: RequestBody, price: RequestBody, image: MultipartBody.Part): Call<ApiResponse> {
        return apiInterface.updateMenu(id, name, sub_category_id, price, image)
    }

    fun deleteMenu(id: Int): Call<ApiResponse> {
        return apiInterface.deleteMenu(id)
    }

    fun getTables(): Call<Tables> {
        return apiInterface.getTables()
    }

    fun addTable(table_no: String): Call<ApiResponse> {
        return apiInterface.addTable(table_no)
    }

    fun updateTable(id: Int, table_no: String): Call<ApiResponse> {
        return apiInterface.updateTable(id, table_no)
    }

    fun deleteTable(id: Int): Call<ApiResponse> {
        return apiInterface.deleteTable(id)
    }

    fun getOrders(): Call<Orders> {
        return apiInterface.getOrders()
    }

    fun getOrderDetails(voucher_no: String): Call<OrderDetails> {
        return apiInterface.getOrderDetails(voucher_no)
    }

    fun addOrder(user_id: Int, table_id: Int, total_price: String, order_detail: String): Call<ApiResponse> {
        return apiInterface.addOrder(user_id, table_id, total_price, order_detail)
    }

    fun getUsers(): Call<Users> {
        return apiInterface.getUsers()
    }

    fun addUser(name: String, email: String, role: String, password: String): Call<ApiResponse> {
        return apiInterface.addUser(name, email, role, password)
    }

    fun deleteUser(id: Int): Call<ApiResponse> {
        return apiInterface.deleteUser(id)
    }
}