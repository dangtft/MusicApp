package com.example.appmusic.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import com.example.appmusic.R

class SongsDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "MusicDatabase.db"
        const val DATABASE_VERSION = 1

        // Table Names
        const val TABLE_ALBUMS = "Albums"
        const val TABLE_SONGS = "Songs"

        // Albums Table Columns
        const val COLUMN_ALBUM_ID = "album_id"
        const val COLUMN_ALBUM_NAME = "album_name"
        const val COLUMN_ALBUM_IMAGE_RES_ID = "album_image_res_id"

        // Songs Table Columns
        const val COLUMN_SONG_ID = "song_id"
        const val COLUMN_SONG_NAME = "song_name"
        const val COLUMN_SONG_ARTIST = "artist"
        const val COLUMN_SONG_DURATION = "duration"
        const val COLUMN_SONG_IMAGE_RES_ID = "song_image_res_id"
        const val COLUMN_SONG_FILE_RES_ID = "song_file_res_id"
        const val COLUMN_ALBUM_ID_FK = "album_id_fk"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createAlbumsTable = """
            CREATE TABLE $TABLE_ALBUMS (
                $COLUMN_ALBUM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ALBUM_NAME TEXT NOT NULL,
                $COLUMN_ALBUM_IMAGE_RES_ID INTEGER
            )
        """.trimIndent()

        val createSongsTable = """
            CREATE TABLE $TABLE_SONGS (
                $COLUMN_SONG_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SONG_NAME TEXT NOT NULL,
                $COLUMN_SONG_ARTIST TEXT,
                $COLUMN_SONG_DURATION INTEGER,
                $COLUMN_SONG_FILE_RES_ID INTEGER,
                $COLUMN_SONG_IMAGE_RES_ID INTEGER,
                $COLUMN_ALBUM_ID_FK INTEGER,
                FOREIGN KEY($COLUMN_ALBUM_ID_FK) REFERENCES $TABLE_ALBUMS($COLUMN_ALBUM_ID)
            )
        """.trimIndent()

        db.execSQL(createAlbumsTable)
        db.execSQL(createSongsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS $TABLE_SONGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ALBUMS")
        onCreate(db)
    }

    // Insert a new album into the database
    fun insertAlbum(db: SQLiteDatabase, albumName: String, albumImageResId: Int): Int {
        val values = ContentValues().apply {
            put(COLUMN_ALBUM_NAME, albumName)
            put(COLUMN_ALBUM_IMAGE_RES_ID, albumImageResId)
        }
        return db.insert(TABLE_ALBUMS, null, values).toInt()
    }

    // Insert a new song into the database
    fun insertSong(db: SQLiteDatabase, songName: String, artist: String, duration: Int, albumId: Int, songFileResId: Int, songImageResId: Int): Int {
        val values = ContentValues().apply {
            put(COLUMN_SONG_NAME, songName)
            put(COLUMN_SONG_ARTIST, artist)
            put(COLUMN_SONG_DURATION, duration)
            put(COLUMN_ALBUM_ID_FK, albumId)
            put(COLUMN_SONG_FILE_RES_ID, songFileResId)
            put(COLUMN_SONG_IMAGE_RES_ID, songImageResId)
        }
        return db.insert(TABLE_SONGS, null, values).toInt()
    }


    // Insert sample data
    fun insertSampleData(db: SQLiteDatabase) {
        val albumId1 = insertAlbum(db, "99 + MCK", R.drawable.mck)
        val albumId2 = insertAlbum(db, "DreAmee", R.drawable.dreamee)

        insertSong(db, "Anh đã ổn hơn", "MCK", 194, albumId1, R.raw.anh_da_on_hon, R.drawable.anhdaonhon)
        insertSong(db, "Chìm sâu", "MCK", 194, albumId1, R.raw.chim_sau, R.drawable.chimsau)
        insertSong(db, "Tại vì sao", "MCK", 194, albumId1, R.raw.tai_vi_sao, R.drawable.taivisao)

        insertSong(db, "Anh nhà ở đâu thế", "Amee", 216, albumId2, R.raw.anh_nha_o_dau_the, R.drawable.anhnhaodauthe)
        insertSong(db, "Đen đá không đường", "Amee", 216, albumId2, R.raw.den_da_khong_duong, R.drawable.dendakhongduong)
        insertSong(db, "Yêu thi yêu không yêu thi yêu", "Amee", 216, albumId2, R.raw.yeu_thi_yeu_khong_yeu_thi_yeu, R.drawable.yeuthiyeukyeuthiyeu)
    }

    // Retrieve all albums from the database
    fun getAlbums(db: SQLiteDatabase): Cursor {
        val query = """
            SELECT $COLUMN_ALBUM_ID,
                   $COLUMN_ALBUM_NAME,
                   $COLUMN_ALBUM_IMAGE_RES_ID
            FROM $TABLE_ALBUMS
        """
        return db.rawQuery(query, null)
    }
    // Retrieve all songs from the database
    public fun getSongs(db: SQLiteDatabase): Cursor {
        val query = """
            SELECT $COLUMN_SONG_ID,
                   $COLUMN_SONG_NAME, 
                   $COLUMN_SONG_ARTIST,
                   $COLUMN_SONG_DURATION, 
                   $COLUMN_SONG_FILE_RES_ID,
                   $COLUMN_SONG_IMAGE_RES_ID
            FROM $TABLE_SONGS
        """.trimIndent()

        return db.rawQuery(query, null)
    }

}
