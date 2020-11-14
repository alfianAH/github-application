package com.dicoding.picodiploma.githubapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.githubapplication.R
import com.dicoding.picodiploma.githubapplication.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter: RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    private val users = ArrayList<User>()

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(user: User){
            with(itemView) {
                tv_username.text = user.username
                tv_name.text = user.name
                tv_location.text = user.location
                Glide.with(this)
                    .load(user.photo)
                    .into(img_photo)

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }

    /**
     * When list is clicked, ...
     */
    interface OnItemClickCallback{
        fun onItemClicked(user: User)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
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

    fun setData(items: ArrayList<User>){
        users.clear()
        users.addAll(items)
        notifyDataSetChanged()
    }
}