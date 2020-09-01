package com.ptzkg.erestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.OrderDetail
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_kitchen.view.*
import kotlinx.android.synthetic.main.item_subtitle.view.*

class KitchenMenuAdapter: RecyclerView.Adapter<KitchenMenuAdapter.KitchenMenuViewHolder>() {
    var orders: List<OrderDetail> = ArrayList()

    inner class KitchenMenuViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(order: OrderDetail) {
            if (order.menu != null) {
                itemView.txt_menu.text = order.menu.name
                itemView.txt_sub_category.text = order.menu.subCategory.name
                itemView.txt_qty.text = "Quantity: "+order.qty
                Picasso.get().load("http://menu-order.khaingthinkyi.me/"+order.menu.image).resize(130, 130).into(itemView.img_menu)
            }
            else {
                itemView.txt_menu.text = order.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KitchenMenuViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_kitchen, parent, false)
        return KitchenMenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: KitchenMenuViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    fun update(orders: List<OrderDetail>) {
        this.orders = orders
        notifyDataSetChanged()
    }
}