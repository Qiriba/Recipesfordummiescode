package com.example.rezeptefuerdummies

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rezeptefuerdummies.fragments.FavoritesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FirebaseFirestore



class MainActivity : AppCompatActivity(), FeedAdapter.OnItemClickListener {

    companion object {
        private const val REQUEST_CODE_POST_NOTIFICATIONS = 101
    }

    private lateinit var searchView: SearchView
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var searchAdapter: FeedAdapter
    private var feedItemList: MutableList<FeedItemModel> = mutableListOf()
    private var searchItemList: MutableList<FeedItemModel> = mutableListOf()

    private val homeFragment = HomeFragment()
    private val favoritesFragment = FavoritesFragment()
    private val profileFragment = ProfileFragment()

    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val searchRecyclerView: RecyclerView = findViewById(R.id.search_recyclerView)
        searchRecyclerView.visibility = View.GONE
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hide default title

        searchView = SearchView(toolbar.context)
        toolbar.addView(searchView)

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                recyclerView.visibility = View.GONE
                searchRecyclerView.visibility = View.VISIBLE
            } else {
                searchRecyclerView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        // Create an empty list for feed items
        var searchItemList: MutableList<FeedItemModel> = mutableListOf()
        // Create and set the adapter
        feedAdapter = FeedAdapter(feedItemList)
        feedAdapter.setOnItemClickListener(this)
        recyclerView.adapter = feedAdapter

        searchAdapter = FeedAdapter(searchItemList)
        searchAdapter.setOnItemClickListener(this)
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

        // Firestore instance
        val db = Firebase.firestore
        getDatabaseData(db,"rezepte") { data ->
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
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    //Füge Dummydata dem adapter hinzu
                    // adapter.addItems(createDummyData())
                }
            }
        })

        requestPostNotificationPermission()

        fragmentManager.beginTransaction().apply {
            add(R.id.fragment_container, profileFragment, "3")
            hide(profileFragment)
            add(R.id.fragment_container, favoritesFragment, "2")
            hide(favoritesFragment)
            add(R.id.fragment_container, homeFragment, "1")
            commit()
        }

        // Hier wird die Navigation Toolbar für den Nutzer definiert
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    switchFragment(homeFragment)
                    true
                }
                R.id.navigation_favorites -> {
                    switchFragment(favoritesFragment)
                    true
                }
                R.id.navigation_profile -> {
                    switchFragment(profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onItemClick(feedItem: FeedItemModel) {
        val intent = Intent(this, RecipeDetailsActivity::class.java).apply {
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
    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            hide(activeFragment)
            show(fragment)
            commit()
        }
        activeFragment = fragment

        // RecyclerViews ausblenden
        findViewById<RecyclerView>(R.id.recyclerView).visibility = View.GONE
        findViewById<RecyclerView>(R.id.search_recyclerView).visibility = View.GONE
        }

        private fun filterData(query: String?) {
        if (query.isNullOrBlank()) {
            searchAdapter.setItems(feedItemList) // Show all items if query is empty
        } else {
            val filteredList = feedItemList.filter { item ->
                // Filter based on the recipeName field (you can adjust this to any other field if needed)
                item.recipeName.contains(query, ignoreCase = true)
            }
            searchAdapter.setItems(filteredList)
        }
    }

    private fun requestPostNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_POST_NOTIFICATIONS)
            } else {
                // Permission already granted, maybe setup notifications here
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, setup or trigger notification here
            } else {
                // Permission denied
            }
        }
    }
    private fun getDatabaseData(db: FirebaseFirestore, collection:String, callback: (MutableList<FeedItemModel>) -> Unit) {
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
                    Log.e(id.toString(),"Recipe ID"+id)

                    val recipeDescription = doc.getString("recipeDescription") ?: ""

                    // Create a FeedItemModel and add it to the list
                    val feedItem = FeedItemModel(imageUrl, recipeName, recipeDifficulty, recipeTime, recipeCategory, id, recipeDescription)
                    feedItemList.add(feedItem)
                }
                callback(feedItemList)
            }
    }
}
