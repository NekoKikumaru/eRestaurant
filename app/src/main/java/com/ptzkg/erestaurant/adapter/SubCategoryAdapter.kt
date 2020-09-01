package com.ptzkg.erestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.SubCategory
import kotlinx.android.synthetic.main.item_subtitle.view.txt_subtitle
import kotlinx.android.synthetic.main.item_title.view.txt_title

class SubCategoryAdapter: RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder>()  {
    var subcategories: List<SubCategory> = ArrayList()

    inner class SubCategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(subcategory: SubCategory) {
            if (subcategory.category != null) {
                itemView.txt_title.text = subcategory.name
                itemView.txt_subtitle.text = subcategory.category.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_subtitle, parent, false)
        return SubCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return subcategories.size
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        holder.bind(subcategories[position])
    }

    fun update(subcategories: List<SubCategory>) {
        this.subcategories = subcategories
        notifyDataSetChanged()
    }

    fun getSubCategoryByPosition(position: Int): SubCategory {
        return subcategories[position]
    }
}