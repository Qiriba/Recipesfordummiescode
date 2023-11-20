import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rezeptefuerdummies.R

class RezeptAdapter(private val rezepte: List<Rezept>) :
    RecyclerView.Adapter<RezeptAdapter.RezeptViewHolder>() {

    class RezeptViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewRezept: ImageView = itemView.findViewById(R.id.imageViewRezept)
        val textViewRezeptTitel: TextView = itemView.findViewById(R.id.textViewRezeptTitel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RezeptViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rezept, parent, false)
        return RezeptViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RezeptViewHolder, position: Int) {
        val aktuellesRezept = rezepte[position]
        holder.textViewRezeptTitel.text = aktuellesRezept.titel
    }

    override fun getItemCount(): Int {
        return rezepte.size
    }
}
