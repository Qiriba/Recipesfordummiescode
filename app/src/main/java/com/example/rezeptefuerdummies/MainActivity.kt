package com.example.rezeptefuerdummies

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.rezeptefuerdummies.fragments.FavoritesFragment
import com.example.rezeptefuerdummies.fragments.HomeFragment
import com.example.rezeptefuerdummies.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), FeedAdapter.OnItemClickListener {

    companion object {
        private const val REQUEST_CODE_POST_NOTIFICATIONS = 101
    }

    private val homeFragment = HomeFragment()
    private val favoritesFragment = FavoritesFragment()
    private val profileFragment = ProfileFragment()

    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPostNotificationPermission()

        fragmentManager.beginTransaction().apply {
            add(R.id.fragment_container, profileFragment, "3")
            hide(profileFragment)
            add(R.id.fragment_container, favoritesFragment, "2")
            hide(favoritesFragment)
            add(R.id.fragment_container, homeFragment, "1")
            commit()
        }

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
    }

    private fun requestPostNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_POST_NOTIFICATIONS)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, setup or trigger notification here
            }
        }
    }
}
