package com.example.whatmovietodayfinal

import android.app.Activity
import android.app.DownloadManager.COLUMN_ID
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
    private var dataLoaded = false // Add a flag to track if data has been loaded

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

        // Load data only if it hasn't been loaded before
        if (!dataLoaded) {
            loadMediaData()
            dataLoaded = true // Set dataLoaded flag to true after loading data
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEDIA_CREATION && resultCode == Activity.RESULT_OK) {
            // Refresh the list after adding new media
            loadMediaData()
        }
    }

    private fun loadMediaData() {
        val dbHelper = DatabaseHelper(requireContext())
        val mediaList = dbHelper.getAllMedia()
        arrayAdapter.clear()
        arrayAdapter.addAll(mediaList)
        arrayAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val REQUEST_MEDIA_CREATION = 100
    }

    // Custom ArrayAdapter with delete functionality
    // Custom ArrayAdapter with delete functionality
    // Custom ArrayAdapter with delete functionality
    // Custom ArrayAdapter with delete functionality
    inner class CustomArrayAdapter(context: Context, objects: ArrayList<String>) :
        ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, objects) {

        // Database constants
        private val TABLE_MEDIA = "media"
        private val COLUMN_ID = "_id"
        private val COLUMN_TITRE = "titre"

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_media, parent, false)
            val currentItem = getItem(position)

            // Set text for the TextViews
            val detailsTextView = view.findViewById<TextView>(R.id.textViewDetails)
            detailsTextView.text = currentItem

            // Event handler for delete button
            view.findViewById<ImageButton>(R.id.buttonDelete).setOnClickListener {
                // Remove the item from the list
                remove(currentItem)
                notifyDataSetChanged()

                // Get the ID of the item to be deleted
                val dbHelper = DatabaseHelper(context)
                val db = dbHelper.writableDatabase
                val cursor = db.rawQuery("SELECT $COLUMN_ID FROM $TABLE_MEDIA WHERE $COLUMN_TITRE=?", arrayOf(currentItem))
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(COLUMN_ID)
                    if (columnIndex != -1) { // Check if columnIndex is valid
                        val id = cursor.getLong(columnIndex)
                        dbHelper.deleteMedia(id)
                    } else {
                        // Handle the case where columnIndex is invalid
                        // Log an error message or take appropriate action
                    }
                }
                cursor.close()
            }
            return view
        }
    }

}