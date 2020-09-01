package com.ptzkg.erestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.Order
import kotlinx.android.synthetic.main.item_invoice.view.*
import kotlin.collections.ArrayList

class InvoiceAdapter: RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder>() {
    var orders: List<Order> = ArrayList()
    var clickListener: ClickListener? = null

    fun setOnClick(clickListener: InvoiceAdapter.ClickListener) {
        this.clickListener = clickListener
    }

    inner class InvoiceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private lateinit var order: Order

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener?.onClick(order)
        }

        fun bind(order: Order) {
            this.order = order
            itemView.txt_voucher.text = order.voucher_no
            itemView.txt_user.text = "Order Taken By: "+order.user_id.name
            itemView.txt_date.text = "Date: "+order.created_at.split("T")[0]
            itemView.txt_total.text = "Total: "+order.total_price+" MMK"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_invoice, parent, false)
        return InvoiceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
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