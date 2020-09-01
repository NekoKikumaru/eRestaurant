package com.ptzkg.erestaurant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.Menu
import com.ptzkg.erestaurant.model.OrderJson
import com.ptzkg.erestaurant.model.SubCategory
import com.ptzkg.erestaurant.viewmodel.OrderViewModel
import kotlinx.android.synthetic.main.item_parent.view.*

class OrderSubCategoryAdapter(context: Context): RecyclerView.Adapter<OrderSubCategoryAdapter.OrderSubCategoryViewHolder>()  {
    var subcategories: List<SubCategory> = ArrayList()
    var menus: List<Menu> = ArrayList()
    var orders: ArrayList<OrderJson> = ArrayList()
    var orderViewModel: OrderViewModel = OrderViewModel()
    var context = context

    inner class OrderSubCategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(subcategory: SubCategory, menu: List<Menu>) {
            itemView.txt_title.text = subcategory.name

            if (subcategory.expanded) {
                itemView.click_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight))
                itemView.expand_icon.setImageResource(R.drawable.ic_baseline_arrow_right)
                itemView.child_recyclerview.visibility = View.VISIBLE
            }
            else {
                itemView.click_layout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
                itemView.expand_icon.setImageResource(R.drawable.ic_baseline_arrow_drop_down)
                itemView.child_recyclerview.visibility = View.GONE
            }

            var menuAdapter = OrderMenuAdapter()
            menuAdapter.updateMenu(menu)
            menuAdapter.updateViewModel(orders, orderViewModel)
            itemView.child_recyclerview.adapter = menuAdapter
            itemView.child_recyclerview.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderSubCategoryViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_parent, parent, false)
        return OrderSubCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return subcategories.size
    }

    override fun onBindViewHolder(holder: OrderSubCategoryViewHolder, position: Int) {
        var subcategory = subcategories[position]
        menus = menus.filter { it.subCategory != null }
        var menu = menus.filter { it.subCategory.id == subcategory.id }
        holder.bind(subcategory, menu)

        holder.itemView.click_layout.setOnClickListener {
            var expanded = subcategory.expanded
            subcategory.expanded = !expanded
            notifyItemChanged(position)
        }
    }

    fun updateSubCategory(subcategories: List<SubCategory>) {
        this.subcategories = subcategories
        notifyDataSetChanged()
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