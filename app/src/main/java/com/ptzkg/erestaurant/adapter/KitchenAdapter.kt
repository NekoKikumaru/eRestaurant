package com.ptzkg.erestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.Order
import kotlinx.android.synthetic.main.item_status.view.*

class KitchenAdapter: RecyclerView.Adapter<KitchenAdapter.KitchenViewHolder>() {
    var orders: List<Order> = ArrayList()
    var clickListener: ClickListener? = null

    fun setOnClick(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    inner class KitchenViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private lateinit var order: Order

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener?.onClick(order)
        }

        fun bind(order: Order) {
            this.order = order
            itemView.txt_title.text = order.voucher_no
            itemView.txt_subtitle.text = "Table: "+order.table.table_no
            itemView.txt_banner.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KitchenViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false)
        return KitchenViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: KitchenViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    fun update(orders: List<Order>) {
        this.orders = orders
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onClick(order: Order)
    }
}