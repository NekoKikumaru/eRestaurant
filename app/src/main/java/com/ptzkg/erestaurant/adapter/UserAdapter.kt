package com.ptzkg.erestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ptzkg.erestaurant.R
import com.ptzkg.erestaurant.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    var users: List<User> = ArrayList()

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.txt_id.text = user.ID_NO
            itemView.txt_role.text = user.role
            itemView.txt_name.text = user.name
            if (user.email != null) {
                itemView.txt_email.text = user.email
            }
            else {
                itemView.txt_email.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun update(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    fun getUserByPosition(position: Int): User {
        return users[position]
    }
}