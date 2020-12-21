package com.shaumaaldo.submissionone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shaumaaldo.submissionone.R
import com.shaumaaldo.submissionone.model.UserFollow

class ItemUserFollowAdapter internal constructor(private val listUser: ArrayList<UserFollow>) :
    RecyclerView.Adapter<ItemUserFollowAdapter.CardViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_follower, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = listUser[position]
        Glide.with(holder.itemView.context)
            .load(item.image)
            .error(R.drawable.imagenotavail)
            .placeholder(R.drawable.pulse_load_image)
            .circleCrop()
            .apply(RequestOptions().override(350, 550))
            .into(holder.imgPhoto)

        holder.tvName.text = item.nameLogin

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }

    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserFollow)
    }

    internal fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_itemuser_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_user_name)
    }
}