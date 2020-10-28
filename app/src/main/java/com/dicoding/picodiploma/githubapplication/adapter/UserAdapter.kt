package com.dicoding.picodiploma.githubapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.User
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter internal constructor(private val users: ArrayList<User>, onUserListener: OnUserClickListener):
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var onUserClickListener = onUserListener

    class ViewHolder internal constructor(view: View, onUserListener: OnUserClickListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener{

        private var onUserClickListener: OnUserClickListener = onUserListener

        private val tvUserName: TextView = view.findViewById(R.id.tv_username)
        private val tvName: TextView = view.findViewById(R.id.tv_name)
        private val tvLocation: TextView = view.findViewById(R.id.tv_location)
        private val imgPhoto: CircleImageView = view.findViewById(R.id.img_photo)

        internal fun bind(user: User){
            tvUserName.text = user.username
            tvName.text = user.name
            tvLocation.text = user.location
            imgPhoto.setImageResource(user.photo)

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onUserClickListener.onClick(adapterPosition)
        }
    }

    /**
     * When list is clicked, ...
     */
    interface OnUserClickListener{
        fun onClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(
            itemView,
            onUserClickListener
        )
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
    }
}