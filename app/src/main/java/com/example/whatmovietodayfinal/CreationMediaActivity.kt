package com.example.whatmovietodayfinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CreationMediaActivity : AppCompatActivity() {

    private lateinit var editTextTitre: EditText
    private lateinit var editTextCategorie: EditText
    private lateinit var editTextGenre: EditText
    private lateinit var editTextAnnee: EditText
    private lateinit var editTextDuree: EditText
    private lateinit var buttonSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_media)

        editTextTitre = findViewById(R.id.editTextTitre)
        editTextCategorie = findViewById(R.id.editTextCategorie)
        editTextGenre = findViewById(R.id.editTextTextGenre)
        editTextAnnee = findViewById(R.id.editTextTextAnnee)
        editTextDuree = findViewById(R.id.editTextTextDuree)
        buttonSubmit = findViewById(R.id.button)

        buttonSubmit.setOnClickListener {
            val titre = editTextTitre.text.toString()
            val categorie = editTextCategorie.text.toString()
            val genre = editTextGenre.text.toString()
            val annee = editTextAnnee.text.toString()
            val duree = editTextDuree.text.toString()

            val intent = Intent()
            intent.putExtra("titre", titre)
            intent.putExtra("categorie", categorie)
            intent.putExtra("genre", genre)
            intent.putExtra("annee", annee)
            intent.putExtra("duree", duree)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
