package com.codeenemy.memorywhisper.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database

import com.codeenemy.memorywhisper.models.HappyPlaceModel
import android.database.sqlite.SQLiteOpenHelper

@Database(entities = [HappyPlaceModel::class], version = 1)
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1 // Database version
        private const val DATABASE_NAME = "HappyPlacesDatabase"
        private const val TABLE_HAPPY_PLACE = "HappyPlacesTable"

        // All the columns names
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_HAPPY_PLACE_TABLE = ("CREATE TABLE " + TABLE_HAPPY_PLACE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT)")
        db?.execSQL(CREATE_HAPPY_PLACE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HAPPY_PLACE")
        onCreate(db)
    }
    fun addHappyPlace(happyPlace: HappyPlaceModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, happyPlace.title)
        contentValues.put(KEY_IMAGE, happyPlace.image)
        contentValues.put(KEY_DESCRIPTION, happyPlace.description)
        contentValues.put(KEY_DATE, happyPlace.date)
        contentValues.put(KEY_LOCATION, happyPlace.location)
        contentValues.put(KEY_LATITUDE, happyPlace.latitude)
        contentValues.put(KEY_LONGITUDE, happyPlace.longitude)

        //Inserting Row
        val result = db.insert(TABLE_HAPPY_PLACE, null, contentValues)
        // 2nd argument is string
        db.close()
        return result
    }

//        fun getInstance(context: Context): DatabaseHandler {
//            synchronized(this) {
//                var instance = INSTANCE
//
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext, DatabaseHandler::class.java,
//                        "happyPlace_database"
//                    ).fallbackToDestructiveMigration().build()
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
}
