package com.example.rezeptefuerdummies

import Rezept
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


    class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            // Create a list of feed items (replace this with your data)
            val feedItemList: List<FeedItemModel> = createDummyData()

            // Create and set the adapter
            val adapter = FeedAdapter(feedItemList)
            recyclerView.adapter = adapter
        }

        // Replace this method with your actual data source
        private fun createDummyData(): List<FeedItemModel> {
            val dummyData = mutableListOf<FeedItemModel>()

            // Add your feed items to the list
            dummyData.add(FeedItemModel(R.drawable.mutyez))
            dummyData.add(FeedItemModel(R.drawable.mutyez))
            dummyData.add(FeedItemModel(R.drawable.mutyez))
            dummyData.add(FeedItemModel(R.drawable.mutyez))
            dummyData.add(FeedItemModel(R.drawable.mutyez))
            return dummyData
        }
    }


