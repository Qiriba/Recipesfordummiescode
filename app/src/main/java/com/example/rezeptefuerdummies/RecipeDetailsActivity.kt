package com.example.rezeptefuerdummies

import android.content.ContentValues.TAG
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.content.Intent
import android.nfc.Tag
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RecipeDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        // Data for the recipe
        val recipeName = intent.getStringExtra("name")
        val recipeDifficulty = intent.getStringExtra("difficulty")
        val recipeTime = intent.getStringExtra("time")
        val recipeCategory = intent.getStringExtra("category")
        val recipeImageResId = intent.getStringExtra("imageUrl")
        val recipeDescription = intent.getStringExtra("description")
        val recipeID = intent.getIntExtra("id",0)
        Log.e(recipeID.toString(),"Recipe ID RecipeDetails"+recipeID)


        // Get references to the views
        val recipeImageView = findViewById<ImageView>(R.id.ard_imageView)
        val recipeNameTextView = findViewById<TextView>(R.id.ard_recipeName)
        val recipeDifficultyTextView = findViewById<TextView>(R.id.ard_recipeDifficulty)
        val recipeTimeTextView = findViewById<TextView>(R.id.ard_recipeTime)
        val recipeCategoryTextView = findViewById<TextView>(R.id.ard_recipeCategory)
        val recipeDescriptionTextView = findViewById<TextView>(R.id.ard_recipeDescription)
        val recipeingredientsView = findViewById<TextView>(R.id.ard_recipeIngredients)

        // Load image using Picasso
        Picasso.get().load(recipeImageResId).into(recipeImageView)

        // Set text for other views
        recipeNameTextView.text = "$recipeName"
        recipeDifficultyTextView.text = "Difficulty: $recipeDifficulty"
        recipeTimeTextView.text = "Time: $recipeTime"
        recipeCategoryTextView.text = "Category: $recipeCategory"
        recipeDescriptionTextView.text = "$recipeDescription"

        // Button to start recipe activity
        val startRecipeButton = findViewById<Button>(R.id.startRecipeButton)
        startRecipeButton.setOnClickListener {
            val intent = Intent(this, StartRecipeActivity::class.java).apply {
                putExtra("id", recipeID) // Verwende hier die lokale Variable recipeID
            }
            startActivity(intent)
        }

        // Firestore query to get ingredients for the recipe with given recipeID
        val db = Firebase.firestore

// Logging to ensure recipeID is correctly set
        Log.d("Firestore Debug", "Querying for recipeID: $recipeID")

        db.collection("rezepte_zutaten")
            .whereEqualTo("recipeID", recipeID)
            .get()
            .addOnSuccessListener { result ->
                val ingredientsList = mutableListOf<FeedIngredientModel>()

                // Check if any documents were returned
                if (result.isEmpty) {
                    Log.d("Firestore Recipe", "No ingredients found for recipeID: $recipeID")
                } else {
                    for (document in result) {
                        Log.d("Firestore Recipe", "Document id: ${document.id} => data: ${document.data}")
                        val ingredientsArray = document.get("rezepte_zutaten") as? List<Map<String, Any>> ?: emptyList()

                        for (ingredientData in ingredientsArray) {
                            val amount = ingredientData["amount"] as? String ?: ""
                            val name = ingredientData["name"] as? String ?: ""
                            val needed = ingredientData["needed"] as? Boolean ?: false

                            val ingredient = FeedIngredientModel(amount, name, needed)
                            ingredientsList.add(ingredient)
                        }
                    }

                    // Show ingredients in TextView (just as an example)
                    recipeingredientsView.text = ingredientsList.joinToString("\n") {
                        "${it.amount} ${it.name}"
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting ingredients: ", exception)
            }


    }


}
