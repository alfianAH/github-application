package com.dicoding.picodiploma.githubapplication

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter internal constructor(private val users: ArrayList<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
//    internal var users = arrayListOf<User>()

//    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
//        var itemView = view
//
//        if(itemView == null){
//            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, viewGroup, false)
//        }
//
//        val viewHolder = ViewHolder(itemView as View)
//
//        val user = getItem(position) as User
//        viewHolder.bind(user)
//        return itemView
//    }
//
//    override fun getItem(i: Int): Any = users[i]
//    override fun getItemId(i: Int): Long = i.toLong()
//    override fun getCount(): Int = users.size

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view){
        private val tvUserName: TextView = view.findViewById(R.id.tv_username)
        private val tvName: TextView = view.findViewById(R.id.tv_name)
        private val tvLocation: TextView = view.findViewById(R.id.tv_location)
        private val imgPhoto: CircleImageView = view.findViewById(R.id.img_photo)

        internal fun bind(user: User){
            tvUserName.text = user.username
            tvName.text = user.name
            tvLocation.text = user.location
            imgPhoto.setImageResource(user.photo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        var itemView = view
//
//        if(itemView == null){
//            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, viewGroup, false)
//        }
//
//        val viewHolder = ViewHolder(itemView as View)
//
//        val user = getItem(position) as User
//        viewHolder.bind(user)
//        return itemView

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }

}