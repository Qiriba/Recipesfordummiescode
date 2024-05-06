package com.example.rezeptefuerdummies

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rezeptefuerdummies.databinding.ActivityRecipeDetailsBinding

class RecipeDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        // Retrieve the recipe ID from the intent
        val recipeId = intent.getIntExtra("id", -1)

        // Find the TextView in the layout
        val textViewId: TextView = findViewById(R.id.textViewId)

        // Display the ID in the TextView
        textViewId.text = "Recipe ID: $recipeId"

        // Now you can use the recipeId to load the actual recipe details
        // and update your UI accordingly
    }


}
