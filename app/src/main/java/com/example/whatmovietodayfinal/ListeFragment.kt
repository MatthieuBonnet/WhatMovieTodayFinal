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
import androidx.fragment.app.Fragment

class ListeFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var arrayAdapter: CustomArrayAdapter
    private var dataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_liste, container, false)

        listView = view.findViewById(R.id.listView)
        arrayAdapter = CustomArrayAdapter(requireContext(), ArrayList())
        listView.adapter = arrayAdapter

        val buttonAddMedia = view.findViewById<Button>(R.id.buttonAddMedia)
        buttonAddMedia.setOnClickListener {
            val intent = Intent(requireActivity(), CreationMediaActivity::class.java)
            startActivityForResult(intent, REQUEST_MEDIA_CREATION)
        }

        if (!dataLoaded) {
            loadMediaData()
            dataLoaded = true
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEDIA_CREATION && resultCode == Activity.RESULT_OK) {
            loadMediaData()
        }
    }

    override fun onResume() {
        super.onResume()
        loadMediaData()
    }

    private fun loadMediaData() {
        val dbHelper = DatabaseHelper(requireContext())
        val mediaList = dbHelper.getAllMedia()
        arrayAdapter.clear()
        arrayAdapter.addAll(mediaList)
        arrayAdapter.notifyDataSetChanged()
    }

    inner class CustomArrayAdapter(context: Context, objects: ArrayList<Media>) :
        ArrayAdapter<Media>(context, android.R.layout.simple_list_item_1, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_media, parent, false)
            val currentItem = getItem(position)

            val titleTextView = view.findViewById<TextView>(R.id.textViewTitle)
            val detailsTextView = view.findViewById<TextView>(R.id.textViewDetails)

            titleTextView.text = currentItem?.titre
            detailsTextView.text = "Catégorie: ${currentItem?.categorie}\nGenre: ${currentItem?.genre}\nAnnée: ${currentItem?.annee}\nDurée: ${currentItem?.duree}"

            titleTextView.setTypeface(null, Typeface.BOLD) // Pour mettre le titre en gras

            view.findViewById<ImageButton>(R.id.buttonDelete).setOnClickListener {
                currentItem?.let { media ->
                    Log.d("CustomArrayAdapter", "Delete button clicked for item: $media")
                    val dbHelper = DatabaseHelper(context)
                    dbHelper.deleteMedia(media.id)

                    // Après la suppression, actualisez la liste des médias
                    loadMediaData()
                }
            }
            return view
        }

    }

    companion object {
        private const val REQUEST_MEDIA_CREATION = 100
    }
}
