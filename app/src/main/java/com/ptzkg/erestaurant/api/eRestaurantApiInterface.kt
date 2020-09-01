package com.ptzkg.erestaurant.api

import com.ptzkg.erestaurant.model.*
import okhttp3.*
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Headers

interface eRestaurantApiInterface {

    @GET("category")
    fun getCategories(): Call<Categories>

    @POST("category")
    fun addCategory(
        @Query("name") name: String
    ): Call<ApiResponse>

    @PUT("category/{id}")
    fun updateCategory(
        @Path("id") id: Int,
        @Query("name") name: String
    ): Call<ApiResponse>

    @DELETE("category/{id}")
    fun deleteCategory(
        @Path("id") id: Int
    ): Call<ApiResponse>

    @GET("sub_category")
    fun getSubCategories(): Call<SubCategories>

    @POST("sub_category")
    fun addSubCategory(
        @Query("name") name: String,
        @Query("category_id") category_id: Int
    ): Call<ApiResponse>

    @PUT("sub_category/{id}")
    fun updateSubCategory(
        @Path("id") id: Int,
        @Query("name") name: String,
        @Query("category_id") category_id: Int
    ): Call<ApiResponse>

    @DELETE("sub_category/{id}")
    fun deleteSubCategory(
        @Path("id") id: Int
    ): Call<ApiResponse>

    @GET("menu")
    fun getMenus(): Call<Menus>

    @Headers("Accept: application/json")
    @Multipart
    @POST("menu")
    fun addMenu(
        @Part("name") name: RequestBody,
        @Part("sub_category_id") sub_category_id: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<ApiResponse>

    @PUT("menu/{id}")
    fun updateMenu(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("sub_category_id") sub_category_id: RequestBody,
        @Part("price") price: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<ApiResponse>

    @DELETE("menu/{id}")
    fun deleteMenu(
        @Path("id") id: Int
    ): Call<ApiResponse>

    @GET("table")
    fun getTables(): Call<Tables>

    @POST("table")
    fun addTable(
        @Query("table_no") table_no: String
    ): Call<ApiResponse>

    @PUT("table/{id}")
    fun updateTable(
        @Path("id") id: Int,
        @Query("table_no") table_no: String
    ): Call<ApiResponse>

    @DELETE("table/{id}")
    fun deleteTable(
        @Path("id") id: Int
    ): Call<ApiResponse>

    @GET("order")
    fun getOrders(): Call<Orders>

    @GET("order/{voucher_no}")
    fun getOrderDetails(
        @Path("voucher_no") voucher_no: String
    ): Call<OrderDetails>

    @POST("order")
    fun addOrder(
        @Query("user_id") user_id: Int,
        @Query("table_id") table_id: Int,
        @Query("total_price") total_price: String,
        @Query("order_detail") order_detail: String
    ): Call<ApiResponse>

    @GET("user")
    fun getUsers(): Call<Users>

    @POST("user")
    fun addUser(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("role") role: String,
        @Query("password") password: String
    ): Call<ApiResponse>

    @DELETE("user/{id}")
    fun deleteUser(
        @Path("id") id: Int
    ): Call<ApiResponse>
}