package com.dicoding.picodiploma.consumerapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.consumerapp.CustomOnItemClickListener
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.activities.DetailActivity
import com.dicoding.picodiploma.consumerapp.databinding.ItemUserBinding
import com.dicoding.picodiploma.consumerapp.entity.User

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
}