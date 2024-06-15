package com.example.rezeptefuerdummies

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.squareup.picasso.Picasso
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class RecipeDetailsActivityTest {

    private lateinit var activity: RecipeDetailsActivity

    @Before
    fun setUp() {
        ShadowLog.stream = System.out // Um Log-Ausgaben in der Konsole zu sehen

        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = Intent(context, RecipeDetailsActivity::class.java).apply {
            putExtra("name", "Test Recipe")
            putExtra("difficulty", "Easy")
            putExtra("time", "30 mins")
            putExtra("category", "Dessert")
            putExtra("imageUrl", "http://example.com/image.jpg")
            putExtra("description", "Test Description")
            putExtra("id", 123)
        }

        activity = Robolectric.buildActivity(RecipeDetailsActivity::class.java, intent).create().resume().get()
    }

    @Test
    fun `test recipe details are displayed correctly`() {
        val recipeNameTextView = activity.findViewById<TextView>(R.id.ard_recipeName)
        val recipeDifficultyTextView = activity.findViewById<TextView>(R.id.ard_recipeDifficulty)
        val recipeTimeTextView = activity.findViewById<TextView>(R.id.ard_recipeTime)
        val recipeCategoryTextView = activity.findViewById<TextView>(R.id.ard_recipeCategory)
        val recipeDescriptionTextView = activity.findViewById<TextView>(R.id.ard_recipeDescription)
        val recipeImageView = activity.findViewById<ImageView>(R.id.ard_imageView)

        assertEquals("Test Recipe", recipeNameTextView.text)
        assertEquals("Difficulty: Easy", recipeDifficultyTextView.text)
        assertEquals("Time: 30 mins", recipeTimeTextView.text)
        assertEquals("Category: Dessert", recipeCategoryTextView.text)
        assertEquals("Test Description", recipeDescriptionTextView.text)

        // Überprüfen, ob das Bild geladen wurde
        Mockito.verify(Picasso.get()).load("http://example.com/image.jpg").into(recipeImageView)
    }
}
