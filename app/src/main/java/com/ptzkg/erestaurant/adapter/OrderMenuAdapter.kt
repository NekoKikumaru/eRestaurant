package com.ptzkg.erestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.Menu
import com.ptzkg.erestaurant.model.OrderJson
import com.ptzkg.erestaurant.viewmodel.OrderViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_child.view.*

class OrderMenuAdapter(): RecyclerView.Adapter<OrderMenuAdapter.OrderMenuViewHolder>() {
    var menus: List<Menu> = ArrayList()
    var orders: ArrayList<OrderJson> = ArrayList()
    var orderViewModel: OrderViewModel = OrderViewModel()

    inner class OrderMenuViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(menu: Menu) {
            Picasso.get().load("http://menu-order.khaingthinkyi.me/"+menu.image).into(itemView.img_menu)
            itemView.txt_menu.text = menu.name
            itemView.txt_price.text = menu.price+" MMK"

            var qty: Int
            var total: Double
            var index = orders.indexOf(orders.find { it.menu_id == menu.id })

            if (index == -1) {
                qty = 0
                total = 0.0
            }
            else {
                qty = orders[index].qty
                total = orders[index].total
            }

            itemView.edit_quantity.setText(qty.toString())
            itemView.total_price.setText(total.toString()+" MMK")

            itemView.btn_add.setOnClickListener {
                qty += 1
                total += menu.price.toDouble()
                orderViewModel.addOrderJson(OrderJson(menu.id, qty, total))

                itemView.edit_quantity.setText(qty.toString())
                itemView.total_price.setText(total.toString()+" MMK")
            }

            itemView.btn_minus.setOnClickListener {
                if (qty != 0) {
                    qty -= 1
                    total -= menu.price.toDouble()
                    orderViewModel.removeOrderJson(OrderJson(menu.id, qty, total))

                    itemView.edit_quantity.setText(qty.toString())
                    itemView.total_price.setText(total.toString()+" MMK")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderMenuViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_child, parent, false)
        return OrderMenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: OrderMenuViewHolder, position: Int) {
        holder.bind(menus[position])
    }

    fun updateMenu(menus: List<Menu>) {
        this.menus = menus
        notifyDataSetChanged()
    }

    fun updateViewModel(orders: ArrayList<OrderJson>, orderViewModel: OrderViewModel) {
        this.orders = orders
        this.orderViewModel = orderViewModel
    }
}