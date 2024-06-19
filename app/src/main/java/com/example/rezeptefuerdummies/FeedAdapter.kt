 package com.example.rezeptefuerdummies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

 class FeedAdapter(private val feedItemList: MutableList<FeedItemModel>) :
     RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
     private var onItemClickListener: OnItemClickListener? = null

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

    interface OnItemClickListener {
        fun onItemClick(feedItem: FeedItemModel)
    }
    override fun getItemCount(): Int = feedItemList.size

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Declare your views for the item layout
        // For example:
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val tvRecipeName: TextView = itemView.findViewById(R.id.tvRecipeName)
        private val tvRecipeDifficulty: TextView = itemView.findViewById(R.id.tvRecipeDifficulty)
        private val tvRecipeTime: TextView = itemView.findViewById(R.id.tvRecipeTime)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvRecipeCategory)

        init {
            imageView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Ensure that the position is valid
                    val clickedItem = feedItemList[position]
                    onItemClickListener?.onItemClick(clickedItem)

                }
            }
        }
        // Create a method to bind data to the views
        // For example:
        fun bind(item: FeedItemModel) {
            // Load the image into imageView
            Picasso.get().load(item.imageUrl).into(imageView)
            tvCategory.text = item.recipeCategory
            tvRecipeName.text = item.recipeName
            tvRecipeTime.text = item.recipeTime
            tvRecipeDifficulty.text = item.recipeDifficulty

        }
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
    fun addItems(newItems: List<FeedItemModel>) {
        feedItemList.addAll(newItems)
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }
     fun setItems(Items: List<FeedItemModel>){
         feedItemList.clear()
         feedItemList.addAll(Items)
         notifyDataSetChanged()
     }

    override fun getItemId(position: Int): Long {
        return feedItemList[position].hashCode().toLong()
    }
}
