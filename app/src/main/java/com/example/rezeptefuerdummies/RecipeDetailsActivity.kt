package com.example.rezeptefuerdummies

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class RecipeDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        val startRecipeButton = findViewById<Button>(R.id.startRecipeButton)
        startRecipeButton.setOnClickListener {
            val intent = Intent(this, StartRecipeActivity::class.java)
            startActivity(intent)
        }

        // Data for the recipe
        val recipeName = intent.getStringExtra("name")
        val recipeDifficulty = intent.getStringExtra("difficulty")
        val recipeTime = intent.getStringExtra("time")
        val recipeCategory = intent.getStringExtra("category")
        val recipeImageResId = intent.getStringExtra("imageUrl")
        val recipeDescription = intent.getStringExtra("description")

        // Get references to the views
        val recipeImageView = findViewById<ImageView>(R.id.ard_imageView)
        val recipeNameTextView = findViewById<TextView>(R.id.ard_recipeName)
        val recipeDifficultyTextView = findViewById<TextView>(R.id.ard_recipeDifficulty)
        val recipeTimeTextView = findViewById<TextView>(R.id.ard_recipeTime)
        val recipeCategoryTextView = findViewById<TextView>(R.id.ard_recipeCategory)
        val recipeDescriptionTextView = findViewById<TextView>(R.id.ard_recipeDescription)

        Picasso.get().load(recipeImageResId).into(recipeImageView)
        recipeNameTextView.text = "$recipeName"
        recipeDifficultyTextView.text = "Difficulty: $recipeDifficulty"
        recipeTimeTextView.text = "Time: $recipeTime"
        recipeCategoryTextView.text = "Category: $recipeCategory"
        recipeDescriptionTextView.text = "$recipeDescription"
    }
    fun startRecipeButtonClick(view: View) {
        val intent = Intent(this, StartRecipeActivity::class.java)
        startActivity(intent)
    }


}
