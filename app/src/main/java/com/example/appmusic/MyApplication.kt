package com.example.appmusic

import android.app.Application
import com.example.appmusic.database.SongsDatabaseHelper

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize database and insert sample data
        val dbHelper = SongsDatabaseHelper(this)
        val db = dbHelper.writableDatabase

        // Check if the database is empty and insert sample data if it is
        val cursor = db.rawQuery("SELECT COUNT(*) FROM ${SongsDatabaseHelper.TABLE_ALBUMS}", null)
        if (cursor.moveToFirst() && cursor.getInt(0) == 0) {
            dbHelper.insertSampleData(db)
        }
        cursor.close()

        val songCursor = db.rawQuery("SELECT COUNT(*) FROM ${SongsDatabaseHelper.TABLE_SONGS}", null)
        if (songCursor.moveToFirst() && songCursor.getInt(0) == 0) {
            dbHelper.insertSampleData(db)
        }
        songCursor.close()
    }
}
