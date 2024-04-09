package com.example.whatmovietodayfinal

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreationMediaActivity : AppCompatActivity() {

    // Déclaration des variables représentant les éléments d'interface utilisateur
    private lateinit var editTextTitre: EditText
    private lateinit var editTextCategorie: EditText
    private lateinit var editTextGenre: EditText
    private lateinit var editTextAnnee: EditText
    private lateinit var editTextDuree: EditText
    private lateinit var buttonSubmit: Button

    // Variable pour stocker l'ID du dernier élément inséré ou modifié
    private var lastInsertedId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_media)

        // Liaison des variables aux éléments de l'interface utilisateur
        editTextTitre = findViewById(R.id.editTextTitre)
        editTextCategorie = findViewById(R.id.editTextCategorie)
        editTextGenre = findViewById(R.id.editTextTextGenre)
        editTextAnnee = findViewById(R.id.editTextTextAnnee)
        editTextDuree = findViewById(R.id.editTextTextDuree)
        buttonSubmit = findViewById(R.id.button)

        // Récupération des données envoyées à cette activité
        val extras = intent.extras
        if (extras != null) {
            // Si des données sont présentes, remplir les champs avec ces données
            editTextTitre.setText(extras.getString("media_titre", ""))
            editTextCategorie.setText(extras.getString("media_categorie", ""))
            editTextGenre.setText(extras.getString("media_genre", ""))
            editTextAnnee.setText(extras.getString("media_annee", ""))
            editTextDuree.setText(extras.getString("media_duree", ""))

            // Modifier le texte du bouton
            buttonSubmit.text = "Modifier"

            // Définir le comportement lors du clic sur le bouton de soumission (modification)
            buttonSubmit.setOnClickListener {
                val titre = editTextTitre.text.toString()
                val categorie = editTextCategorie.text.toString()
                val genre = editTextGenre.text.toString()
                val annee = editTextAnnee.text.toString()
                val duree = editTextDuree.text.toString()

                // Accès à la base de données pour mettre à jour le média
                val dbHelper = DatabaseHelper(this)
                dbHelper.updateMedia(
                    extras.getLong("media_id", -1),
                    titre,
                    categorie,
                    genre,
                    annee,
                    duree
                )

                // Afficher un message de succès
                showToast("Média modifié avec succès")

                // Envoyer un résultat à l'activité appelante et terminer cette activité
                setResult(Activity.RESULT_OK)
                finish()
            }
        } else {
            // Si aucune donnée n'est présente, définir le comportement lors du clic sur le bouton de soumission (ajout)
            buttonSubmit.setOnClickListener {
                val titre = editTextTitre.text.toString()
                val categorie = editTextCategorie.text.toString()
                val genre = editTextGenre.text.toString()
                val annee = editTextAnnee.text.toString()
                val duree = editTextDuree.text.toString()

                // Accès à la base de données pour insérer un nouveau média
                val dbHelper = DatabaseHelper(this)
                val insertedId = dbHelper.insertMedia(titre, categorie, genre, annee, duree)

                // Si l'insertion a réussi, mettre à jour l'ID du dernier élément inséré
                if (insertedId != -1L) {
                    lastInsertedId = insertedId
                }

                // Afficher un message de succès
                showToast("Média ajouté avec succès")

                // Envoyer un résultat à l'activité appelante et terminer cette activité
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    // Fonction pour afficher un Toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Fonction pour obtenir l'ID du dernier élément inséré
    fun getLastInsertedId(): Long {
        return lastInsertedId
    }
}
