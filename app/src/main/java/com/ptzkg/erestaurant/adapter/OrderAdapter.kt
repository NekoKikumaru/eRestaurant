package com.ptzkg.erestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.Table
import kotlinx.android.synthetic.main.item_order.view.*

class OrderAdapter: RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    var tables: List<Table> = ArrayList()
    var clickListener: ClickListener? = null

    fun setOnClick(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private lateinit var table: Table

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener?.onClick(table)
        }

        fun bind(table: Table) {
            this.table = table
            itemView.txt_table.text = table.table_no

            if (table.status.toInt() == 0) {
                itemView.txt_status.text = "Empty"
                itemView.txt_status.setBackgroundResource(R.drawable.ribbon_banner_red)
            }
            else {
                itemView.txt_status.text = "Occupied"
                itemView.txt_status.setBackgroundResource(R.drawable.ribbon_banner_blue)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tables.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(tables[position])
    }

    fun update(tables: List<Table>) {
        this.tables = tables
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onClick(table: Table)
    }
}