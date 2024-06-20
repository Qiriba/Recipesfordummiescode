package com.example.rezeptefuerdummies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FeedAdapter(private var feedItemList: MutableList<FeedItemModel>) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(feedItem: FeedItemModel)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val feedItem = feedItemList[position]
        holder.bind(feedItem)
    }

    override fun getItemCount(): Int = feedItemList.size

    fun setItems(newItems: List<FeedItemModel>) {
        feedItemList.clear()
        feedItemList.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val tvRecipeName: TextView = itemView.findViewById(R.id.tvRecipeName)
        private val tvRecipeDifficulty: TextView = itemView.findViewById(R.id.tvRecipeDifficulty)
        private val tvRecipeTime: TextView = itemView.findViewById(R.id.tvRecipeTime)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvRecipeCategory)

        fun bind(item: FeedItemModel) {
            Picasso.get().load(item.imageUrl).into(imageView)
            tvCategory.text = item.recipeCategory
            tvRecipeName.text = item.recipeName
            tvRecipeTime.text = item.recipeTime
            tvRecipeDifficulty.text = item.recipeDifficulty

            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(item)
            }
        }
    }
}
