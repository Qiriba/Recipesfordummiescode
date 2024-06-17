package com.example.rezeptefuerdummies

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class StartRecipeActivity : AppCompatActivity() {

    // Firebase Firestore Instanz
    private val db = FirebaseFirestore.getInstance()

    private lateinit var titleTextView: TextView
    private lateinit var stepTextView: TextView
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button

    private var currentStepIndex = 0
    private var stepArray = emptyList<StepItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_recipe)

        titleTextView = findViewById(R.id.asr_titleTextView)
        stepTextView = findViewById(R.id.asr_stepTextView)
        nextButton = findViewById(R.id.asr_nextButton)
        prevButton = findViewById(R.id.asr_prevButton)

        nextButton.setOnClickListener {
            nextButtonClick()
        }

        prevButton.setOnClickListener {
            prevButtonClick()
        }

        val recipeID = intent.getIntExtra("id", 0)
        Log.e("Recipe ID", "RecipeStartRecipe $recipeID")
        fetchStepsFromFirestore("XjANct78kjI8K5Dq9OMX")
    }

    private fun fetchStepsFromFirestore(documentId: String) {
        // Firestore query for the specific document in the "rezepte_step" collection
        db.collection("rezepte_step").document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("Firestore", "Document ID: ${document.id}")

                    // Assuming the structure that "step" is a list of maps
                    val steps = document.get("step") as? List<Map<String, Any>>

                    steps?.let {
                        stepArray = it.map { stepData ->
                            val imageUrl = stepData["imageURL"] as? String ?: ""
                            val title = stepData["title"] as? String ?: ""
                            val content = stepData["content"] as? String ?: ""
                            StepItem(title, imageUrl, content)
                        }
                        Log.d("Firestore", "Steps loaded successfully: $stepArray")
                        updateStepTextView()
                    }

                    if (stepArray.isEmpty()) {
                        Log.d("Firestore", "No steps available.")
                        stepTextView.text = "No steps available."
                    }
                } else {
                    Log.d("Firestore", "No document found with ID: $documentId")
                    stepTextView.text = "No steps available."
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting document: ", exception)
            }
    }




    private fun updateStepTextView() {
        // Sicherstellen, dass currentStepIndex gültig bleibt
        currentStepIndex = currentStepIndex.coerceIn(0, stepArray.size - 1)
        // Hier den Text im TextView basierend auf currentStepIndex und stepArray aktualisieren
        if (currentStepIndex < stepArray.size) {
            Log.d("Firestore", "Updating stepTextView to: ${stepArray[currentStepIndex].content}")
            runOnUiThread {
                stepTextView.text = stepArray[currentStepIndex].content
            }
        } else {
            Log.d("Firestore", "No steps available. Updating stepTextView to default message.")
            runOnUiThread {
                stepTextView.text = "No steps available."
            }
        }
    }


    private fun nextButtonClick() {
        currentStepIndex++
        updateStepTextView()
    }

    private fun prevButtonClick() {
        currentStepIndex--
        updateStepTextView()
    }
}
