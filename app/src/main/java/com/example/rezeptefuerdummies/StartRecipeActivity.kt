package com.example.rezeptefuerdummies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class StartRecipeActivity : AppCompatActivity() {

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

        fetchStepsFromFirestore(recipeID)
    }

    private fun fetchStepsFromFirestore(recipeID:Int) {
        // Firestore Abfrage für die Sammlung "rezepte_step"
        db.collection("rezepte_step")
            .whereEqualTo("recipeID",recipeID)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.d("Firestore", "No documents found.")
                    return@addOnSuccessListener
                }

                for (document in result) {
                    Log.d("Firestore", "Document ID: ${document.id}")

                    // Annahme der Struktur, dass "step" eine Liste von Maps ist
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
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
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
                titleTextView.text = stepArray[currentStepIndex].name


            }

            // Überprüfen, ob der nächste Schritt der letzte Schritt ist
            if (currentStepIndex == stepArray.size - 1) {
                runOnUiThread {
                    nextButton.text = "Finish"
                    nextButton.setOnClickListener {
                        endRecipe()
                    }
                }
            } else {
                runOnUiThread {
                    nextButton.text = "Next"
                }
            }
        } else {
            Log.d("Firestore", "No steps available. Updating stepTextView to default message.")
            runOnUiThread {
                stepTextView.text = "No steps available."
            }
        }
    }

    private fun endRecipe() {
        showFinishConfirmationDialog()
    }

    private fun nextButtonClick() {
        currentStepIndex++
        updateStepTextView()
    }

    private fun showFinishConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Finish Recipe")
        builder.setMessage("Are you sure you want to finish this recipe?")

        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()
            navigateToMainActivity()
        }

        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun prevButtonClick() {
        currentStepIndex--
        updateStepTextView()
        nextButton.setOnClickListener {
            nextButtonClick()
        }
    }
}
