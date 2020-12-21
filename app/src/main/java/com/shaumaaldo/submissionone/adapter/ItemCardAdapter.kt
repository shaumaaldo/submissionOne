package com.shaumaaldo.submissionone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shaumaaldo.submissionone.R
import com.shaumaaldo.submissionone.model.Item

class ItemCardAdapter internal constructor(private val listItem: ArrayList<Item>) :
    RecyclerView.Adapter<ItemCardAdapter.CardViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_view, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = listItem[position]
        Glide.with(holder.itemView.context)
            .load(item.avatar)
            .error(R.drawable.imagenotavail)
            .placeholder(R.drawable.pulse_load_image)
            .circleCrop()
            .apply(RequestOptions().override(350, 550))
            .into(holder.imgPhoto)

        holder.tvName.text = item.username
        holder.tvDetail.text = item.name

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listItem[holder.adapterPosition]) }


    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Item)
    }

    internal fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var tvDetail: TextView = itemView.findViewById(R.id.tv_item_detail)
    }

}