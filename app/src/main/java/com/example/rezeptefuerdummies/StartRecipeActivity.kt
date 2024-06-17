package com.example.rezeptefuerdummies

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.util.*

class StartRecipeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private lateinit var titleTextView: TextView
    private lateinit var stepTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var nextButton: Button
    private lateinit var btnSetTimer: Button
    private lateinit var tvTimer: TextView
    private lateinit var prevButton: Button

    private var currentStepIndex = 0
    private var stepArray = emptyList<StepItem>()
    private var countdownTimer: CountDownTimer? = null
    private var timeInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_recipe)

        titleTextView = findViewById(R.id.asr_titleTextView)
        stepTextView = findViewById(R.id.asr_stepTextView)
        imageView = findViewById(R.id.asr_imageView)
        nextButton = findViewById(R.id.asr_nextButton)
        prevButton = findViewById(R.id.asr_prevButton)
        btnSetTimer = findViewById(R.id.btnSetTimer)
        tvTimer = findViewById(R.id.tvTimer)

        nextButton.setOnClickListener {
            nextButtonClick()
        }

        prevButton.setOnClickListener {
            prevButtonClick()
        }

        btnSetTimer.setOnClickListener {
            showTimePickerDialog()
        }

        val recipeID = intent.getIntExtra("id", 0)
        Log.e("Recipe ID", "RecipeStartRecipe $recipeID")

        fetchStepsFromFirestore(recipeID)
    }

    private fun fetchStepsFromFirestore(recipeID: Int) {
        // Firestore Abfrage für die Sammlung "rezepte_step"
        db.collection("rezepte_step")
            .whereEqualTo("recipeID", recipeID)
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
                            val isTimerNeeded = stepData["isTimerNeeded"] as? Boolean ?: false
                            StepItem(title, imageUrl, content, isTimerNeeded)
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
            val step = stepArray[currentStepIndex]

            runOnUiThread {
                stepTextView.text = step.content
                titleTextView.text = step.name

                if (step.imageUrl.isNotEmpty()) {
                    Picasso.get()
                        .load(step.imageUrl)
                        .into(imageView)
                }

                if (step.isTimerNeeded) {
                    btnSetTimer.visibility = Button.VISIBLE
                    tvTimer.visibility = TextView.VISIBLE
                    btnSetTimer.setOnClickListener {
                        showTimePickerDialog()
                    }
                } else {
                    btnSetTimer.visibility = Button.GONE
                    tvTimer.visibility = TextView.GONE

                }
            }

            // Überprüfen, ob der nächste Schritt der letzte Schritt ist
            if (currentStepIndex == stepArray.size - 1) {
                runOnUiThread {
                    nextButton.text = "Finish"
                    nextButton.setOnClickListener {
                        showFinishConfirmationDialog()
                    }
                }
            } else {
                runOnUiThread {
                    nextButton.text = "Next"
                    nextButton.setOnClickListener {
                        nextButtonClick()
                    }
                }
            }
        } else {
            Log.d("Firestore", "No steps available. Updating stepTextView to default message.")
            runOnUiThread {
                stepTextView.text = "No steps available."
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedMinute, selectedSecond ->
                startTimer(selectedMinute, selectedSecond)
            },
            minute,
            second,
            true
        )
        timePickerDialog.show()
    }

    private fun startTimer(selectedMinutes: Int, selectedSeconds: Int) {
        // Umrechnung der ausgewählten Minuten und Sekunden in Millisekunden
        val totalTimeInMillis = (selectedMinutes * 60 + selectedSeconds) * 1000L

        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeInMillis = millisUntilFinished
                updateTimerUI()
            }

            override fun onFinish() {
                timeInMillis = 0
                updateTimerUI()
                // Hier können Sie die Logik hinzufügen, die nach Ablauf des Timers ausgeführt werden soll
            }
        }.start()
    }

    private fun updateTimerUI() {
        val minutes = (timeInMillis / 60000).toInt()
        val seconds = ((timeInMillis % 60000) / 1000).toInt()

        val timeString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        tvTimer.text = timeString
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
