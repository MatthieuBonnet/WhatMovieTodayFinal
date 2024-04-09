package com.example.whatmovietodayfinal

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment

class HistoriqueFragment : Fragment() {

    // Déclarations des variables
    private lateinit var listView: ListView
    private lateinit var arrayAdapter: CustomArrayAdapter
    private val filteredMediaList = ArrayList<Media>() // Liste filtrée pour les médias avec historique = 1
    private var dataLoaded = false

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate du layout pour ce fragment
        val view = inflater.inflate(R.layout.fragment_historique, container, false)

        // Initialisation de la ListView et de son adaptateur
        listView = view.findViewById(R.id.listViewHistorique)
        arrayAdapter = CustomArrayAdapter(requireContext(), filteredMediaList)
        listView.adapter = arrayAdapter

        // Références des boutons de défilement
        val buttonScrollUp = view.findViewById<Button>(R.id.buttonScrollUpHistorique)
        val buttonScrollDown = view.findViewById<Button>(R.id.buttonScrollDownHistorique)

        // Écouteurs d'événements pour le défilement
        buttonScrollUp.setOnClickListener {
            listView.smoothScrollByOffset(-1) // Défilement vers le haut
        }

        buttonScrollDown.setOnClickListener {
            listView.smoothScrollByOffset(1) // Défilement vers le bas
        }

        // Chargement des données si ce n'est pas déjà fait
        if (!dataLoaded) {
            loadHistoriqueData()
            dataLoaded = true
        }

        return view
    }

    // Méthode pour charger les données d'historique depuis la base de données
    private fun loadHistoriqueData() {
        val dbHelper = DatabaseHelper(requireContext())
        val mediaList = dbHelper.getAllMedia().filter { it.historique == 1 } // Filtrer les médias avec historique = 1
        filteredMediaList.clear() // Effacer la liste filtrée avant de la remplir à nouveau
        filteredMediaList.addAll(mediaList)
        arrayAdapter.notifyDataSetChanged()
    }

    // Classe adaptateur personnalisée pour la ListView
    inner class CustomArrayAdapter(context: Context, objects: ArrayList<Media>) :
        ArrayAdapter<Media>(context, android.R.layout.simple_list_item_1, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_media, parent, false)
            val currentItem = getItem(position)

            // Références aux éléments de la vue
            val titleTextView = view.findViewById<TextView>(R.id.textViewTitle)
            val detailsTextView = view.findViewById<TextView>(R.id.textViewDetails)

            // Affichage des informations du média dans les TextView correspondants
            titleTextView.text = currentItem?.titre
            detailsTextView.text = "Catégorie: ${currentItem?.categorie}\nGenre: ${currentItem?.genre}\nAnnée: ${currentItem?.annee}\nDurée: ${currentItem?.duree}"

            titleTextView.setTypeface(null, Typeface.BOLD) // Mettre le titre en gras

            // Gestionnaire de clic pour le bouton de suppression du média
            view.findViewById<ImageButton>(R.id.buttonDelete).setOnClickListener {
                currentItem?.let { media ->
                    Log.d("CustomArrayAdapter", "Bouton supprimer appuyé pour : $media")
                    val dbHelper = DatabaseHelper(context)
                    dbHelper.deleteMedia(media.id)

                    // Après la suppression, actualiser la liste des médias
                    loadHistoriqueData()
                }
            }

            return view
        }
    }
}
