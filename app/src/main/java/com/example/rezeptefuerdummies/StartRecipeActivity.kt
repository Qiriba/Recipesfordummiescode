package com.example.rezeptefuerdummies

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StartRecipeActivity : AppCompatActivity() {
    var currentStepIndex = 0
    var stepArray = emptyArray<stepItem>()
    val recipeID = intent.getStringExtra("id")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_recipe)

        val nextButton = findViewById<Button>(R.id.asr_nextButton)
        nextButton.setOnClickListener {
            nextButtonClick()
        }

        val prevButton = findViewById<Button>(R.id.asr_prevButton)
        prevButton.setOnClickListener {
            prevButtonClick()
        }
        val titleTV = findViewById<TextView>(R.id.asr_titleTextView)
        val stepTV = findViewById<TextView>(R.id.asr_stepTextView)
    }

    fun nextButtonClick(){
        currentStepIndex++
    }

    fun prevButtonClick(){
        currentStepIndex--
    }

}