package com.example.rezeptefuerdummies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class FeedAdapter(private val feedItemList: List<FeedItemModel>) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        // Bind your data to the views in the item layout
        // For example:
        // holder.bind(feedItemList[position])
        val currentItem = feedItemList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = feedItemList.size

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare your views for the item layout
        // For example:
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        // Create a method to bind data to the views
        // For example:
        fun bind(item: FeedItemModel) {
            // Load the image into imageView
             // Picasso.get().load(item.imageUrl).into(imageView)
            imageView.setImageResource(item.imageUrl)
        }

    }
}
