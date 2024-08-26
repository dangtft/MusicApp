package com.example.appmusic

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmusic.activity.PlaySongActivity
import com.example.appmusic.database.Album
import com.example.appmusic.database.Song
import com.example.appmusic.database.SongsDatabaseHelper
import com.example.appmusic.ui.theme.AppMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppMusicTheme {
                Surface {
                    MusicAppLayout(SongsDatabaseHelper(this))
                }
            }
        }
    }
}

@Composable
fun MusicAppLayout(databaseHelper: SongsDatabaseHelper) {
    val context = LocalContext.current
    val db = remember { databaseHelper.readableDatabase }

    // Fetch songs and albums from the database
    val songs by remember { mutableStateOf(fetchSongs(databaseHelper)) }
    val albums by remember { mutableStateOf(fetchAlbums(databaseHelper)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TopHeader()

            SearchBox()

            FavoritesSection(albums)

            MusicListSection(songs, context)
        }
        BottomNavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(56.dp)
        )

    }

}

@SuppressLint("ResourceAsColor")
@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(R.color.main_grey))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .border(2.dp, Color.White, shape = RoundedCornerShape(80))
                .clip(RoundedCornerShape(50))
                .background(Color.Transparent)
        ) {
            IconButton(
                onClick = { /* TODO: Handle onClick */ },
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.music_note),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { /* TODO: Handle onClick */ },
            modifier = Modifier.size(60.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.menu_3gach),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun SearchBox() {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .background(Color.LightGray, RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                if (searchQuery.text.isEmpty()) {
                    Text("Search for music", color = Color.Gray)
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun FavoritesSection(albums: List<Album>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(8.dp)
                .height(160.dp)
        ) {
            albums.forEach { album ->
                AlbumItem(album)
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }
}

@Composable
fun AlbumItem(album: Album) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .size(110.dp, 180.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, Color.Gray)
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = album.albumImageResId),
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .size(100.dp)
        )

        Icon(
            painter = painterResource(id = R.drawable.star_solid),
            contentDescription = null,
            tint = Color.Blue,
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.TopStart)
                .padding(4.dp)
                .border(2.dp, Color.Blue, shape = RoundedCornerShape(80))
        )
    }
}

@Composable
fun MusicListSection(songs: List<Song>, context: android.content.Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "MUSIC LIST",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0000FF)) // Replace with your color resource
                .padding(10.dp)
                .clip(RoundedCornerShape(topStart = 30.dp, bottomStart = 30.dp))
        )

        songs.forEach { song ->
            SongItem(song, context)
        }
    }
}

@Composable
fun SongItem(song: Song, context: android.content.Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable {
                val intent = Intent(context, PlaySongActivity::class.java).apply {
                    putExtra("COLUMN_SONG_NAME", song.songName)
                    putExtra("COLUMN_SONG_ARTIST", song.artist)
                    putExtra("COLUMN_SONG_DURATION", song.duration)
                    putExtra("COLUMN_SONG_FILE_RES_ID", song.songFileResId)
                    putExtra("COLUMN_SONG_IMAGE_RES_ID", song.imageResId)
                    putExtra("COLUMN_ALBUM_ID_FK", song.albumId)
                }
                context.startActivity(intent)
            }
    ) {
        Image(
            painter = painterResource(id = song.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.songName,
                fontSize = 16.sp,
                color = Color(0xFFF3C4E4), // Replace with your color resource
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = song.artist, // Display song artist
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.width(60.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.duration.toString(), // Display song duration
                fontSize = 26.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        Icon(
            Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFF0000FF), // Replace with your color resource
            modifier = Modifier
                .size(40.dp)
                .padding(top = 10.dp)
        )
    }
}

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(50.dp))
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val icons = listOf(
                Icons.Default.Home,
                Icons.Default.Search,
                Icons.Default.Favorite,
                Icons.Default.AccountCircle
            )
            icons.forEachIndexed { index, icon ->
                IconButton(onClick = { selectedIndex = index }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (selectedIndex == index) Color.Blue else Color.Gray
                    )
                }
            }
        }
    }
}

fun fetchSongs(databaseHelper: SongsDatabaseHelper): List<Song> {
    val songList = mutableListOf<Song>()
    val db = databaseHelper.readableDatabase
    val cursor: Cursor = db.rawQuery("SELECT * FROM Songs", null)
    if (cursor.moveToFirst()) {
        do {
            val song = Song(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("song_id")),
                songName = cursor.getString(cursor.getColumnIndexOrThrow("song_name")),
                artist = cursor.getString(cursor.getColumnIndexOrThrow("artist")),
                duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration")),
                songFileResId = cursor.getInt(cursor.getColumnIndexOrThrow("song_file_res_id")),
                imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("song_image_res_id")),
                albumId = cursor.getInt(cursor.getColumnIndexOrThrow("album_id_fk"))
            )
            songList.add(song)
        } while (cursor.moveToNext())
    }
    cursor.close()
    db.close()
    return songList
}

fun fetchAlbums(databaseHelper: SongsDatabaseHelper): List<Album> {
    val albumList = mutableListOf<Album>()
    val db = databaseHelper.readableDatabase
    val cursor: Cursor = db.rawQuery("SELECT * FROM Albums", null)
    if (cursor.moveToFirst()) {
        do {
            val album = Album(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("album_id")),
                albumName = cursor.getString(cursor.getColumnIndexOrThrow("album_name")),
                albumImageResId = cursor.getInt(cursor.getColumnIndexOrThrow("album_image_res_id"))
            )
            albumList.add(album)
        } while (cursor.moveToNext())
    }
    cursor.close()
    db.close()
    return albumList
}
