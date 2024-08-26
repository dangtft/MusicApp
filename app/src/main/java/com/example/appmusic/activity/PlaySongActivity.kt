package com.example.appmusic.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.appmusic.R
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView


class PlaySongActivity : ComponentActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var playButton: ImageView
    private lateinit var nextButton: ImageView
    private lateinit var previousButton: ImageView
    private lateinit var shuffleButton: ImageView
    private lateinit var rotateButton: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeTextView: TextView
    private  lateinit var backButton : ImageButton

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)

        val songTitle = intent.getStringExtra("COLUMN_SONG_NAME")
        val artist = intent.getStringExtra("COLUMN_SONG_ARTIST")
        val songFileResId = intent.getIntExtra("COLUMN_SONG_FILE_RES_ID", 0)
        val songImageResId = intent.getIntExtra("COLUMN_SONG_IMAGE_RES_ID", 0)

        val songTitleTextView = findViewById<TextView>(R.id.songTitle)
        val songImageView = findViewById<ImageView>(R.id.songImage)
        val songArtist = findViewById<TextView>(R.id.artistName)
        startTimeTextView = findViewById(R.id.startTime)
        endTimeTextView = findViewById(R.id.endTime)
        seekBar = findViewById(R.id.progressBar)

        playButton = findViewById(R.id.play)
        nextButton = findViewById(R.id.next)
        previousButton = findViewById(R.id.previous)
        shuffleButton = findViewById(R.id.shuffle)
        rotateButton = findViewById(R.id.rotate)
        backButton = findViewById(R.id.btn_back)

        songTitleTextView.text = songTitle
        if (songImageResId != 0) {
            songImageView.setImageResource(songImageResId)
        }

        songArtist.text = artist

        mediaPlayer = MediaPlayer.create(this, songFileResId)
        mediaPlayer.start()


        endTimeTextView.text = formatTime(mediaPlayer.duration)
        seekBar.max = mediaPlayer.duration


        updateSeekBar()

        nextButton.setOnClickListener {
            // Implement skipping to the next song
        }
        backButton.setOnClickListener {
            finish()
        }

        previousButton.setOnClickListener {
            // Implement skipping to the previous song
        }

        // Shuffle and Rotate functionality (dummy implementations)
        shuffleButton.setOnClickListener {
            // Implement shuffle functionality
        }

        playButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playButton.setImageResource(R.drawable.circle_play_solid)
            } else {
                mediaPlayer.start()
                playButton.setImageResource(R.drawable.circle_pause_solid)
            }
        }


        rotateButton.setOnClickListener {
            // Implement rotate functionality
        }

        // SeekBar change listener
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun updateSeekBar() {
        val update = object : Runnable {
            override fun run() {
                if (mediaPlayer.isPlaying) {
                    seekBar.progress = mediaPlayer.currentPosition
                    startTimeTextView.text = formatTime(mediaPlayer.currentPosition)
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(update)
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        handler.removeCallbacksAndMessages(null)
    }
}






