package com.example.whatmovietodayfinal

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import android.widget.Toast
import androidx.fragment.app.Fragment

class ListeFragment : Fragment() {

    // Déclarations des variables
    private lateinit var listView: ListView
    private lateinit var arrayAdapter: CustomArrayAdapter
    private val filteredMediaList = ArrayList<Media>()
    private var dataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate du layout pour ce fragment
        val view = inflater.inflate(R.layout.fragment_liste, container, false)

        // Initialisation de la ListView et de son adaptateur
        listView = view.findViewById(R.id.listView)
        arrayAdapter = CustomArrayAdapter(requireContext(), filteredMediaList)
        listView.adapter = arrayAdapter

        // Références des boutons de défilement
        val buttonScrollUp = view.findViewById<Button>(R.id.buttonScrollUp)
        val buttonScrollDown = view.findViewById<Button>(R.id.buttonScrollDown)

        // Écouteurs d'événements pour le défilement
        buttonScrollUp.setOnClickListener {
            listView.smoothScrollByOffset(-1) // Défilement vers le haut
        }

        buttonScrollDown.setOnClickListener {
            listView.smoothScrollByOffset(1) // Défilement vers le bas
        }

        // Écouteur d'événement pour le bouton d'ajout de média
        val buttonAddMedia = view.findViewById<Button>(R.id.buttonAddMedia)
        buttonAddMedia.setOnClickListener {
            val intent = Intent(requireActivity(), CreationMediaActivity::class.java)
            startActivityForResult(intent, REQUEST_MEDIA_CREATION)
        }

        // Chargement des données si ce n'est pas déjà fait
        if (!dataLoaded) {
            loadMediaData()
            dataLoaded = true
        }

        return view
    }

    // Méthode appelée lorsqu'un résultat est renvoyé par une activité démarrée pour obtenir un résultat
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Si le résultat provient de l'activité de création de média et est OK, recharger les données
        if (requestCode == REQUEST_MEDIA_CREATION && resultCode == Activity.RESULT_OK) {
            loadMediaData()
        }
    }

    // Méthode appelée lorsque le fragment redevient visible
    override fun onResume() {
        super.onResume()
        loadMediaData()
    }

    // Méthode pour charger les données de médias à partir de la base de données
    private fun loadMediaData() {
        val dbHelper = DatabaseHelper(requireContext())
        val mediaList = dbHelper.getAllMedia()
        // Filtrer les médias avec historique = 0 (non archivés)
        filteredMediaList.clear()
        filteredMediaList.addAll(mediaList.filter { it.historique == 0 })
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
                    Log.d("CustomArrayAdapter", "Bouton supprimer cliqué pour : $media")
                    val dbHelper = DatabaseHelper(context)
                    dbHelper.deleteMedia(media.id)
                    // Après la suppression, actualiser la liste des médias
                    loadMediaData()
                    showToast("Média supprimé avec succès")
                }
            }

            // Gestionnaire de clic pour le bouton d'archivage du média
            view.findViewById<ImageButton>(R.id.buttonArchive).setOnClickListener {
                currentItem?.let { media ->
                    Log.d("CustomArrayAdapter", "Bouton archiver cliqué pour : $media")
                    val dbHelper = DatabaseHelper(context)
                    dbHelper.archiveMedia(media.id)
                    // Après l'archivage, actualiser la liste des médias
                    loadMediaData()
                    showToast("Média archivé avec succès")
                }
            }

            // Gestionnaire de clic pour le bouton d'édition du média
            view.findViewById<ImageButton>(R.id.buttonEdit).setOnClickListener {
                currentItem?.let { media ->
                    Log.d("CustomArrayAdapter", "Bouton modifier cliqué pour : $media")
                    val intent = Intent(context, CreationMediaActivity::class.java).apply {
                        // Passer les données du média à l'activité de création pour modification
                        putExtra("media_id", media.id)
                        putExtra("media_titre", media.titre)
                        putExtra("media_categorie", media.categorie)
                        putExtra("media_genre", media.genre)
                        putExtra("media_annee", media.annee)
                        putExtra("media_duree", media.duree)
                    }
                    context.startActivity(intent)
                }
            }

            return view
        }
    }

    // Méthode pour afficher un toast
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_MEDIA_CREATION = 100
    }
}
