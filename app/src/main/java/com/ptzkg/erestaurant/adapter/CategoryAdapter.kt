package com.ptzkg.erestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.Category
import kotlinx.android.synthetic.main.item_title.view.*

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>()  {
    var categories: List<Category> = ArrayList()

    inner class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) {
            itemView.txt_title.text = category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_title, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    fun update(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    fun getCategoryByPosition(position: Int): Category {
        return categories[position]
    }
}