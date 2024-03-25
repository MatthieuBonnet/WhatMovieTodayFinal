package com.example.whatmovietodayfinal

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ListeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_liste, container, false)

        val buttonAddMedia = view.findViewById<Button>(R.id.buttonAddMedia)
        buttonAddMedia.setOnClickListener {
            val intent = Intent(requireActivity(), CreationMediaActivity::class.java)
            startActivityForResult(intent, REQUEST_MEDIA_CREATION)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEDIA_CREATION && resultCode == RESULT_OK) {
            val genre = data?.getStringExtra("genre")
            val annee = data?.getStringExtra("annee")
            val duree = data?.getStringExtra("duree")

            val textViewGenre = requireView().findViewById<TextView>(R.id.textViewGenre)
            val textViewAnnee = requireView().findViewById<TextView>(R.id.textViewAnnee)
            val textViewDuree = requireView().findViewById<TextView>(R.id.textViewDuree)

            textViewGenre.text = genre
            textViewAnnee.text = annee
            textViewDuree.text = duree
        }
    }

    companion object {
        private const val REQUEST_MEDIA_CREATION = 100
    }
}
