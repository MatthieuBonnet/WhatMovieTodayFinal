package com.example.whatmovietodayfinal

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Déclaration des constantes pour la version de la base de données, son nom et le nom de la table
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "MediaDatabase.db"
        private const val TABLE_MEDIA = "media"

        // Déclaration des noms de colonnes dans la table
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TITRE = "titre"
        private const val COLUMN_CATEGORIE = "categorie"
        private const val COLUMN_GENRE = "genre"
        private const val COLUMN_ANNEE = "annee"
        private const val COLUMN_DUREE = "duree"
        private const val COLUMN_HISTORIQUE = "historique"
    }

    // Méthode appelée lors de la création de la base de données
    override fun onCreate(db: SQLiteDatabase?) {
        // Création de la table de médias avec les colonnes spécifiées
        val createTableQuery = ("CREATE TABLE $TABLE_MEDIA (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TITRE TEXT," +
                "$COLUMN_CATEGORIE TEXT," +
                "$COLUMN_GENRE TEXT," +
                "$COLUMN_ANNEE TEXT," +
                "$COLUMN_DUREE TEXT," +
                "$COLUMN_HISTORIQUE INTEGER DEFAULT 0)")
        db?.execSQL(createTableQuery)
    }

    // Méthode appelée lors de la mise à jour de la base de données
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Suppression de la table existante et recréation de la base de données
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MEDIA")
        onCreate(db)
    }

    // Méthode pour insérer un nouveau média dans la base de données
    fun insertMedia(titre: String, categorie: String, genre: String, annee: String, duree: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITRE, titre)
        contentValues.put(COLUMN_CATEGORIE, categorie)
        contentValues.put(COLUMN_GENRE, genre)
        contentValues.put(COLUMN_ANNEE, annee)
        contentValues.put(COLUMN_DUREE, duree)
        val id = db.insert(TABLE_MEDIA, null, contentValues)
        db.close()
        return id
    }

    // Méthode pour récupérer tous les médias de la base de données
    @SuppressLint("Range")
    fun getAllMedia(): ArrayList<Media> {
        val mediaList = ArrayList<Media>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_MEDIA", null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndex(COLUMN_ID))
                val titre = it.getString(it.getColumnIndex(COLUMN_TITRE))
                val categorie = it.getString(it.getColumnIndex(COLUMN_CATEGORIE))
                val genre = it.getString(it.getColumnIndex(COLUMN_GENRE))
                val annee = it.getString(it.getColumnIndex(COLUMN_ANNEE))
                val duree = it.getString(it.getColumnIndex(COLUMN_DUREE))
                val historique = it.getInt(it.getColumnIndex(COLUMN_HISTORIQUE))
                mediaList.add(Media(id, titre, categorie, genre, annee, duree, historique))
            }
        }
        db.close()
        return mediaList
    }

    // Méthode pour supprimer un média de la base de données
    fun deleteMedia(id: Long) {
        val db = this.writableDatabase
        val rowsAffected = db.delete(TABLE_MEDIA, "$COLUMN_ID = ?", arrayOf(id.toString()))
        Log.d("DatabaseHelper", "Ligne affectée: $rowsAffected")
        db.close()
    }

    // Méthode pour archiver un média dans la base de données
    fun archiveMedia(id: Long) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_HISTORIQUE, 1)
        }
        val rowsAffected = db.update(TABLE_MEDIA, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
        Log.d("DatabaseHelper", "Ligne affectée par l'archivage: $rowsAffected")
        db.close()
    }

    // Méthode pour mettre à jour les informations d'un média dans la base de données
    fun updateMedia(id: Long, titre: String, categorie: String, genre: String, annee: String, duree: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITRE, titre)
            put(COLUMN_CATEGORIE, categorie)
            put(COLUMN_GENRE, genre)
            put(COLUMN_ANNEE, annee)
            put(COLUMN_DUREE, duree)
        }
        val rowsAffected = db.update(TABLE_MEDIA, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
        Log.d("DatabaseHelper", "Ligne affectée par la mise à jour du média: $rowsAffected")
        db.close()
    }
}
