package com.example.rezeptefuerdummies

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FeedAdapter(private val feedItemList: MutableList<FeedItemModel>) :
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

        init {
            // Set an OnClickListener for the ImageView
            imageView.setOnClickListener {
                // Handle the click event here
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Ensure that the position is valid
                    val clickedItem = feedItemList[position]
                    // Handle the click on the clickedItem
                    // For example, you might open a detailed view or perform some action
                }
            }
        }
        // Create a method to bind data to the views
        // For example:
        fun bind(item: FeedItemModel) {
            // Load the image into imageView
            Picasso.get().load(item.imageUrl).into(imageView)
        }

    }
    fun addItems(newItems: List<FeedItemModel>) {
        feedItemList.addAll(newItems)
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }

    override fun getItemId(position: Int): Long {
        return feedItemList[position].hashCode().toLong()
    }
}
