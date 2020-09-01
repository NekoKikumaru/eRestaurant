package com.ptzkg.erestaurant.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.Category
import com.ptzkg.erestaurant.model.Menu
import com.ptzkg.erestaurant.model.OrderJson
import com.ptzkg.erestaurant.model.SubCategory
import com.ptzkg.erestaurant.viewmodel.OrderViewModel
import kotlinx.android.synthetic.main.item_parent.view.*

class OrderCategoryAdapter(context: Context): RecyclerView.Adapter<OrderCategoryAdapter.OrderCategoryViewHolder>() {
    var categories: List<Category> = ArrayList()
    var subcategories: List<SubCategory> = ArrayList()
    var menus: List<Menu> = ArrayList()
    var orders: ArrayList<OrderJson> = ArrayList()
    var orderViewModel: OrderViewModel = OrderViewModel()
    var context = context

    inner class OrderCategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category, subcategory: List<SubCategory>, menu: List<Menu>) {
            itemView.txt_title.text = category.name
            if (category.expanded) {
                itemView.click_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                itemView.expand_icon.setImageResource(R.drawable.ic_baseline_arrow_right)
                itemView.expand_icon.setColorFilter(android.R.color.white)
                itemView.txt_title.setTextColor(ContextCompat.getColor(context, android.R.color.white))
                itemView.child_recyclerview.visibility = View.VISIBLE
            }
            else {
                itemView.click_layout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
                itemView.expand_icon.setImageResource(R.drawable.ic_baseline_arrow_drop_down)
                itemView.expand_icon.setColorFilter(R.color.colorPrimary)
                itemView.txt_title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                itemView.child_recyclerview.visibility = View.GONE
            }

            var subCategoryAdapter = OrderSubCategoryAdapter(context)
            subCategoryAdapter.updateSubCategory(subcategory)
            subCategoryAdapter.updateMenu(menu)
            subCategoryAdapter.updateViewModel(orders, orderViewModel)

            itemView.child_recyclerview.adapter = subCategoryAdapter
            itemView.child_recyclerview.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderCategoryViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_parent, parent, false)
        return OrderCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: OrderCategoryViewHolder, position: Int) {
        var category = categories[position]

        subcategories = subcategories.filter { it.category != null }
        var subcategory = subcategories.filter { it.category.id == category.id }
        holder.bind(category, subcategory, menus)

        holder.itemView.click_layout.setOnClickListener {
            var expanded = category.expanded
            category.expanded = !expanded
            notifyItemChanged(position)
        }
    }

    fun updateCategory(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
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