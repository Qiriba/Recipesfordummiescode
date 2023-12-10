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
            val feedItemList: MutableList<FeedItemModel> = createDummyData()

            // Create and set the adapter
            val adapter = FeedAdapter(feedItemList)
            recyclerView.adapter = adapter

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        // Load more data (e.g., fetch the next page of objects)
                        adapter.addItems(createDummyData())
                    }

                }
            })

        }

        // Replace this method with your actual data source
        private fun createDummyData(): MutableList<FeedItemModel> {
            val dummyData = mutableListOf<FeedItemModel>()

            // Add your feed items to the list
            dummyData.add(FeedItemModel("https://karlsruhepuls.de/wp-content/uploads/2023/06/Thai-Food-Festival-Karlsruhe.jpg", "Thai Curry", "Hard", "45 Min", "Klassisch", 1))
            dummyData.add(FeedItemModel("https://www.chefsculinar.de/chefsculinar/ds_img/assets_700/2014-09-04-Doener-690x460.jpg", "Döner Kebap", "Medium", "1 H", "Klassisch", 2))
            dummyData.add(FeedItemModel("https://assets.tmecosys.com/image/upload/t_web767x639/img/recipe/ras/Assets/b36fbe87cb4d6e6e3dce4b23aa35e481/Derivates/563a4efc4ab575cad5db7a9279096132b4334a7c.jpg", "Deutsche Rumpsteak", "Hard", "45 Min", "Klassisch", 3))
            dummyData.add(FeedItemModel("https://www.lidl-kochen.de/images/recipe-wide/860844/veganer-gyrosteller-mit-reis-und-salat-311084.jpg", "Veganer Gyros Teller", "Easy", "30 Min", "Vegan", 4))
            dummyData.add(FeedItemModel("https://img.chefkoch-cdn.de/rezepte/2529831396465550/bilder/1509532/crop-960x720/pfannkuchen-crepe-und-pancake.jpg", "Omas Pfannkuchen", "Easy", "20 Min", "Vegetarisch", 5))
            return dummyData
        }
    }


