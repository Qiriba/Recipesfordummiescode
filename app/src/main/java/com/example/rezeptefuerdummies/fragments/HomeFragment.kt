package com.example.rezeptefuerdummies.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rezeptefuerdummies.FeedAdapter
import com.example.rezeptefuerdummies.FeedItemModel
import com.example.rezeptefuerdummies.R
import com.example.rezeptefuerdummies.RecipeDetailsActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(), FeedAdapter.OnItemClickListener {

    private lateinit var searchView: SearchView
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var searchAdapter: FeedAdapter
    private var feedItemList: MutableList<FeedItemModel> = mutableListOf()
    private var searchItemList: MutableList<FeedItemModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val searchRecyclerView: RecyclerView = view.findViewById(R.id.search_recyclerView)
        searchView = view.findViewById(R.id.search_view)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false) // Hide default title

        recyclerView.layoutManager = LinearLayoutManager(context)
        searchRecyclerView.layoutManager = LinearLayoutManager(context)
        searchRecyclerView.visibility = View.GONE

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                recyclerView.visibility = View.GONE
                searchRecyclerView.visibility = View.VISIBLE
            } else {
                searchRecyclerView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        feedAdapter = FeedAdapter(feedItemList)
        feedAdapter.setOnItemClickListener(this)
        searchAdapter = FeedAdapter(searchItemList)
        searchAdapter.setOnItemClickListener(this)
        recyclerView.adapter = feedAdapter
        searchRecyclerView.adapter = searchAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchItemList = feedItemList
                filterData(newText)
                return true
            }
        })

        val db = Firebase.firestore
        getDatabaseData(db, "rezepte") { data ->
            feedItemList.addAll(data)
            feedAdapter.notifyDataSetChanged()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Add dummy data to the adapter
                    // feedAdapter.addItems(createDummyData())
                }
            }
        })

        return view
    }

    private fun filterData(query: String?) {
        if (query.isNullOrBlank()) {
            searchAdapter.setItems(feedItemList)
        } else {
            val filteredList = feedItemList.filter { item ->
                item.recipeName.contains(query, ignoreCase = true)
            }
            searchAdapter.setItems(filteredList)
        }
    }

    private fun getDatabaseData(db: FirebaseFirestore, collection: String, callback: (MutableList<FeedItemModel>) -> Unit) {
        val feedItemList: MutableList<FeedItemModel> = mutableListOf()
        db.collection(collection)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    Log.d("Firestore", "id: ${doc.id} - data: ${doc.data}")
                    val recipeName = doc.getString("recipeName") ?: ""
                    val recipeTime = doc.getString("recipeTime") ?: ""
                    val recipeDifficulty = doc.getString("recipeDifficulty") ?: ""
                    val recipeCategory = doc.getString("recipeCategory") ?: ""
                    val imageUrl = doc.getString("imageUrl") ?: ""
                    val id = doc.getLong("recipeID")?.toInt() ?: 0
                    val recipeDescription = doc.getString("recipeDescription") ?: ""

                    // Create a FeedItemModel and add it to the list
                    val feedItem = FeedItemModel(
                        imageUrl, recipeName, recipeDifficulty, recipeTime,
                        recipeCategory, id, recipeDescription
                    )
                    feedItemList.add(feedItem)
                }
                callback(feedItemList)
            }
    }

    override fun onItemClick(feedItem: FeedItemModel) {
        val intent = Intent(activity, RecipeDetailsActivity::class.java).apply {
            putExtra("id", feedItem.recipeID)
            putExtra("name", feedItem.recipeName)
            putExtra("time", feedItem.recipeTime)
            putExtra("difficulty", feedItem.recipeDifficulty)
            putExtra("category", feedItem.recipeCategory)
            putExtra("imageUrl", feedItem.imageUrl)
            putExtra("description", feedItem.recipeDescription)
        }
        startActivity(intent)
    }
}
