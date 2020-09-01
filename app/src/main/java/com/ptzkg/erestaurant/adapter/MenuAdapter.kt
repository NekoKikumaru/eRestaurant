package com.ptzkg.erestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.Menu
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_menu.view.*

class MenuAdapter(): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    var menus: List<Menu> = ArrayList()

    inner class MenuViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(menu: Menu) {
            if (menu.subCategory != null) {
                itemView.txt_menu.text = menu.name
                itemView.txt_sub_category.text = menu.subCategory.name
                itemView.txt_price.text = menu.price+" MMK"
                Picasso.get().load("http://menu-order.khaingthinkyi.me/"+menu.image).resize(130, 130).into(itemView.img_menu)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menus[position])
    }

    fun update(menus: List<Menu>) {
        this.menus = menus
        notifyDataSetChanged()
    }

    fun getMenuByPosition(position: Int): Menu {
        return menus[position]
    }
}