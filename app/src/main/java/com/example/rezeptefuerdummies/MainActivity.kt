import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rezeptefuerdummies.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Ihr Titel"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // RecyclerView für die Rezeptliste initialisieren
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewRezepte)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RezeptAdapter(getBeispielRezepte()) // Implementieren Sie diese Funktion

    }

    // Funktion zum Abrufen von Beispiel-Rezepten
    private fun getBeispielRezepte(): List<Rezept> {

        return listOf(
            Rezept("Spaghetti Bolognese"),
            Rezept("Caprese Salat"),
            Rezept("Schokoladenkuchen")

        )
    }
}
