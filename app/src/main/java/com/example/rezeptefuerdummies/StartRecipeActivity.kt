package com.example.rezeptefuerdummies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class StartRecipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_recipe)

        val recipeID = intent.getStringExtra("id")

    }
}