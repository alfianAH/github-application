package com.dicoding.picodiploma.githubapplication.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.githubapplication.CustomOnItemClickListener
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.activities.DetailActivity
import com.dicoding.picodiploma.githubapplication.databinding.ItemUserBinding
import com.dicoding.picodiploma.githubapplication.entity.User

class UserAdapter(private val activity: Activity): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var users = ArrayList<User>()
        set(listUsers){
            if(listUsers.size > 0){
                users.clear()
            }

            users.addAll(listUsers)
            notifyDataSetChanged()
        }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = ItemUserBinding.bind(itemView)
        fun bind(user: User){
            with(itemView) {
                binding.tvUsername.text = user.username
                binding.tvName.text = user.name
                binding.tvLocation.text = user.location
                Glide.with(this)
                    .load(user.photo)
                    .into(binding.imgPhoto)

                itemView.setOnClickListener(CustomOnItemClickListener(adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback{
                    override fun onItemClicked(view: View, position: Int) {
                        val moveIntent = Intent(activity, DetailActivity::class.java)

                        moveIntent.putExtra(DetailActivity.EXTRA_URL_PROFILE, user.urlProfile)
                        moveIntent.putExtra(DetailActivity.EXTRA_USER, user)
                        moveIntent.putExtra(DetailActivity.EXTRA_POSITION, position)

                        activity.startActivityForResult(moveIntent, DetailActivity.REQUEST_UPDATE)
                    }
                }))
            }
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

    /**
     * Add item to Recycler View
     */
    fun addItem(user: User){
        users.add(user)
        notifyItemInserted(users.size - 1)
    }

    /**
     * Update item in Recycler View
     */
    fun updateItem(position: Int, user: User){
        users[position] = user
        notifyItemChanged(position, user)
    }

    /**
     * Remove item from Recycler View
     */
    fun removeItem(position: Int){
        users.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, users.size)
    }
}