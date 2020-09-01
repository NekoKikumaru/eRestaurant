package com.ptzkg.erestaurant.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.Table
import kotlinx.android.synthetic.main.item_title.view.*

class TableAdapter: RecyclerView.Adapter<TableAdapter.TableViewHolder>() {
    var tables: List<Table> = ArrayList()

    inner class TableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(table: Table) {
            itemView.txt_title.text = table.table_no
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_title, parent, false)
        return TableViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tables.size
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bind(tables[position])
    }

    fun update(tables: List<Table>) {
        this.tables = tables
        notifyDataSetChanged()
    }

    fun getTableByPosition(position: Int): Table {
        return tables[position]
    }
}