package com.example.rezeptefuerdummies

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartRecipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_recipe)
        val nextButton = findViewById<Button>(R.id.asr_nextButton)
        nextButton.setOnClickListener {
            nextButtonClick()
        }
        val prevButton = findViewById<Button>(R.id.asr_prevButton)
        nextButton.setOnClickListener {
            prevButtonClick()
        }
        val recipeID = intent.getStringExtra("id")

    }
    fun nextButtonClick(){

    }

    fun prevButtonClick(){

    }
}