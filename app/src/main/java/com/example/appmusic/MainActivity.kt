package com.example.appmusic

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
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

        val cursor = remember { databaseHelper.getSongs(db) }
        val songs by remember { mutableStateOf(cursorToSongs(cursor)) }

        val cursorAlbums = remember { databaseHelper.getAlbums(db) }
        val albums by remember { mutableStateOf(cursorToAlbums(cursorAlbums)) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = colorResource(id = R.color.main_pink))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left button with white circular border
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .border(2.dp, Color.White, shape = RoundedCornerShape(80))
                            .clip(RoundedCornerShape(50))
                            .background(Color.Transparent)
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
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

                    Spacer(modifier = Modifier.weight(1f)) // Spacer to push the right button to the end

                    // Right button with white color
                    IconButton(
                        onClick = { /*TODO*/ },
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

                // Search Box
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
                                Text("Search for music", color = Color.White)
                            }
                            innerTextField()
                        }
                    )
                }

                // Favorites Section
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

                                // Icon in the top-left corner
                                Icon(
                                    painter = painterResource(id = R.drawable.star_solid),
                                    contentDescription = null,
                                    tint = Color.Blue,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .align(Alignment.TopStart)
                                        .padding(4.dp)
                                        .border(
                                            2.dp,
                                            Color.Blue,
                                            shape = RoundedCornerShape(80)
                                        )
                                )
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                        }
                    }
                }

                // Music List Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 30.dp,
                                bottomStart = 30.dp
                            )
                        )
                        .background(colorResource(id = R.color.main_blue))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "MUSIC LIST",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    songs.forEach { song ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .padding(vertical = 10.dp)
                                .clickable {
                                    // Navigate to PlaySongActivity with the selected song
                                    val intent =
                                        Intent(context, PlaySongActivity::class.java).apply {
                                            putExtra("COLUMN_SONG_NAME", song.songName)
                                            putExtra("COLUMN_SONG_ARTIST", song.artist)
                                            putExtra("COLUMN_SONG_DURATION", song.duration)
                                            putExtra("COLUMN_SONG_FILE_RES_ID", song.songFileResId)
                                            putExtra("COLUMN_SONG_IMAGE_RES_ID", song.imageResId)
                                        }
                                    context.startActivity(intent)
                                }
                        ) {
                            Icon(
                                painter = painterResource(id = song.imageResId),
                                contentDescription = null,
                                tint = colorResource(id = R.color.main_blue),
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(top = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = song.songName,
                                    fontSize = 16.sp,
                                    color = colorResource(id = R.color.main_pink),
                                    modifier = Modifier.padding(8.dp)
                                )
                                Text(
                                    text = song.duration.toString(), // Display song duration
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
                                tint = colorResource(id = R.color.main_blue),
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(top = 10.dp)
                            )
                        }
                    }
                }
            }

            // Navigation Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .shadow(8.dp, RoundedCornerShape(50.dp)) // Add shadow here
                    .clip(RoundedCornerShape(50.dp))
                    .background(color = colorResource(id = R.color.white))
                    .align(Alignment.BottomCenter) // Position at the bottom
            ) {
                var selectedIndex by remember { mutableStateOf(0) }
                NavigationBar(
                    containerColor = colorResource(id = R.color.white),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Home",
                                tint = colorResource(id = R.color.main_blue),
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        selected = selectedIndex == 0,
                        onClick = { selectedIndex = 0 }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = colorResource(id = R.color.main_blue),
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        selected = selectedIndex == 1,
                        onClick = { selectedIndex = 1 }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.star_solid),
                                contentDescription = "Star",
                                tint = colorResource(id = R.color.main_blue),
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        selected = selectedIndex == 2,
                        onClick = { selectedIndex = 2 }
                    )
                }
            }
        }
    }


    private fun cursorToSongs(cursor: Cursor): List<Song> {
        val songs = mutableListOf<Song>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(SongsDatabaseHelper.COLUMN_SONG_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(SongsDatabaseHelper.COLUMN_SONG_NAME))
            val artist = cursor.getString(cursor.getColumnIndexOrThrow(SongsDatabaseHelper.COLUMN_SONG_ARTIST))
            val duration = cursor.getInt(cursor.getColumnIndexOrThrow(SongsDatabaseHelper.COLUMN_SONG_DURATION))
            val songResId = cursor.getInt(cursor.getColumnIndexOrThrow(SongsDatabaseHelper.COLUMN_SONG_FILE_RES_ID))
            val imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(SongsDatabaseHelper.COLUMN_SONG_IMAGE_RES_ID))
            val albumId = cursor.getInt(cursor.getColumnIndexOrThrow(SongsDatabaseHelper.COLUMN_ALBUM_ID))
            songs.add(Song(id, title,artist, duration,songResId, imageResId,albumId))
        }
        cursor.close()
        return songs
    }
    private fun cursorToAlbums(cursor: Cursor): List<Album> {
        val albums = mutableListOf<Album>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(SongsDatabaseHelper.COLUMN_ALBUM_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(SongsDatabaseHelper.COLUMN_ALBUM_NAME))
            val coverImageResId = cursor.getInt(cursor.getColumnIndexOrThrow(SongsDatabaseHelper.COLUMN_ALBUM_IMAGE_RES_ID))
            albums.add(Album(id, name, coverImageResId))
        }
        cursor.close()
        return albums
    }



