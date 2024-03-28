package com.example.whatmovietodayfinal



import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "MediaDatabase.db"
        private const val TABLE_MEDIA = "media"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TITRE = "titre"
        private const val COLUMN_CATEGORIE = "categorie"
        private const val COLUMN_GENRE = "genre"
        private const val COLUMN_ANNEE = "annee"
        private const val COLUMN_DUREE = "duree"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_MEDIA (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TITRE TEXT," +
                "$COLUMN_CATEGORIE TEXT," +
                "$COLUMN_GENRE TEXT," +
                "$COLUMN_ANNEE TEXT," +
                "$COLUMN_DUREE TEXT)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MEDIA")
        onCreate(db)
    }

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

    fun getAllMedia(): ArrayList<String> {
        val mediaList = ArrayList<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_MEDIA", null)
        cursor.use {
            while (it.moveToNext()) {
                val titreIndex = it.getColumnIndex(COLUMN_TITRE)
                val categorieIndex = it.getColumnIndex(COLUMN_CATEGORIE)
                val genreIndex = it.getColumnIndex(COLUMN_GENRE)
                val anneeIndex = it.getColumnIndex(COLUMN_ANNEE)
                val dureeIndex = it.getColumnIndex(COLUMN_DUREE)

                val titre = it.getString(titreIndex)
                val categorie = it.getString(categorieIndex)
                val genre = it.getString(genreIndex)
                val annee = it.getString(anneeIndex)
                val duree = it.getString(dureeIndex)

                mediaList.add("$titre - $categorie - $genre - $annee - $duree")
            }
        }
        db.close()
        return mediaList
    }

    fun deleteMedia(id: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_MEDIA, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

}
