package com.ptzkg.erestaurant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ptzkg.erestaurant.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderViewModel: BaseViewModel() {
    var orderJsons = ArrayList<OrderJson>()
    var orders: MutableLiveData<List<Order>> = MutableLiveData()
    var orderDetails: MutableLiveData<List<OrderDetail>> = MutableLiveData()

    fun getOrderJson(): ArrayList<OrderJson> = orderJsons
    fun getOrders(): LiveData<List<Order>> = orders
    fun getOrderDetails(): LiveData<List<OrderDetail>> = orderDetails

    fun addOrderJson(order: OrderJson) {
        var index = orderJsons.indexOf(orderJsons.find { it.menu_id == order.menu_id })
        if (index == -1) {
            orderJsons.add(order)
        }
        else {
            orderJsons[index] = order
        }
    }

    fun removeOrderJson(order: OrderJson) {
        var index = orderJsons.indexOf(orderJsons.find { it.menu_id == order.menu_id })
        orderJsons[index] = order
        if (orderJsons[index].qty == 0) {
            orderJsons.removeAt(index)
        }
    }

    fun loadOrders() {
        loading.value = true
        loadError.value = false
        val call = api.getOrders()

        call.enqueue(object : Callback<Orders> {
            override fun onFailure(call: Call<Orders>, t: Throwable) {
                loading.value = false
                loadError.value = true
            }

            override fun onResponse(call: Call<Orders>, response: Response<Orders>) {
                response.isSuccessful.let {
                    loading.value = false
                    orders.value = response.body()?.orders
                }
            }
        })
    }

    fun loadOrderDetails(voucher_no: String) {
        loading.value = true
        loadError.value = false
        val call = api.getOrderDetails(voucher_no)

        call.enqueue(object : Callback<OrderDetails> {
            override fun onFailure(call: Call<OrderDetails>, t: Throwable) {
                loading.value = false
                loadError.value = true
            }

            override fun onResponse(call: Call<OrderDetails>, response: Response<OrderDetails>) {
                response.isSuccessful.let {
                    loading.value = false
                    orderDetails.value = response.body()?.order_detail
                }
            }
        })
    }

    fun addOrder(user_id: Int, table_id: Int, total_price: String, order_detail: String) {
        message.value = ""
        var call = api.addOrder(user_id, table_id, total_price, order_detail)

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