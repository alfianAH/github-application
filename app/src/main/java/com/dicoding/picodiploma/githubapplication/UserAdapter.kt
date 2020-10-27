package com.dicoding.picodiploma.githubapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter internal constructor(private val users: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

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
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }
}