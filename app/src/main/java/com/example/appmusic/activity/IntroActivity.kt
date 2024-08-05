package com.example.appmusic.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmusic.R
import com.example.appmusic.ui.theme.AppMusicTheme

class IntroActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppMusicTheme {
                IntroScreen {
                    startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
                }
            }
        }
    }
}

@Composable
fun IntroScreen(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3C4E4))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(R.drawable.lovepik_com_380591529_listening_to_music_vector_cartoon_sticker),
                contentDescription = null,
                modifier = Modifier.size(331.dp, 403.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "MUSIC",
                //fontFamily = AutourOne,
                fontSize = 34.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "FEEL GOOD",
                //fontFamily = BlackOpsOne,
                fontSize = 48.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomButton(onClick = onStartClick)
        }
    }
}

@Composable
fun CustomButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color(0xFFF3C4E4), shape = RoundedCornerShape(50.dp))
            .border(2.dp, Color.White, shape = RoundedCornerShape(50.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "GO",
            fontSize = 34.sp,
            color = Color.White,
            //fontFamily = AutourOne
        )
    }
}


