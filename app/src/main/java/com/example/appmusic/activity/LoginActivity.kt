package com.example.appmusic.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmusic.R
import com.example.appmusic.databinding.ActivityLoginBinding
import com.example.appmusic.ui.theme.AppMusicTheme
import com.google.firebase.auth.FirebaseAuth
class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContent {
            AppMusicTheme {
                LoginScreen(auth)
            }
        }
    }
}

@Composable
fun LoginScreen(auth: FirebaseAuth) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3C4E4))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("LOGIN", fontSize = 34.sp, color = Color.White)

            Spacer(modifier = Modifier.height(32.dp))

            BasicTextField(
                value = email.value,
                onValueChange = { email.value = it },
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(25.dp))
                    .padding(16.dp)
                    .fillMaxWidth(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        if (email.value.isEmpty()) {
                            Text("Email", color = Color.Gray)
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = password.value,
                onValueChange = { password.value = it },
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(25.dp))
                    .padding(16.dp)
                    .fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        if (password.value.isEmpty()) {
                            Text("Password", color = Color.Gray)
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(text = "Login", onClick = {
                auth.signInWithEmailAndPassword(email.value, password.value)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            })

            Spacer(modifier = Modifier.height(16.dp))

            Text("OR", color = Color.White, fontSize = 20.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                IconButton(onClick = { /* Handle Facebook login */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.facebook_brands_solid),
                        contentDescription = "Facebook",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = { /* Handle Google login */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.google_brands_solid),
                        contentDescription = "Google",
                        modifier = Modifier.size(40.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("You don't have an account?", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Register",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(context, RegisterActivity::class.java))
                    }
                )
            }
        }
    }
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF3C4E4),
    textColor: Color = Color.White,
    textSize: Float = 20f,
    cornerRadius: Float = 25f,
    strokeWidth: Float = 2f,
    strokeColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerRadius.dp)
            )
            .border(
                border = BorderStroke(strokeWidth.dp, strokeColor),
                shape = RoundedCornerShape(cornerRadius.dp)
            )
            .clickable(onClick = onClick)
            .padding(17.dp)
           ,
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = textSize.sp
        )
    }
}


