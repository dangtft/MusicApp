package com.example.appmusic.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.autoSaver
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
import com.example.appmusic.ui.theme.AppMusicTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContent {
            AppMusicTheme {
                RegisterScreen(auth)
            }
        }
    }
}

@Composable
fun RegisterScreen(auth: FirebaseAuth){
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

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
            Text("Register", fontSize = 34.sp, color = Color.White)

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
            BasicTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
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
                        if (confirmPassword.value.isEmpty()) {
                            Text("Confirm Password", color = Color.Gray)
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(text = "Register", onClick = {
                when {
                    email.value.isEmpty() -> {
                        errorMessage.value = "Email cannot be empty"
                    }
                    password.value.isEmpty() -> {
                        errorMessage.value = "Password cannot be empty"
                    }
                    confirmPassword.value.isEmpty() -> {
                        errorMessage.value = "Confirm Password cannot be empty"
                    }
                    password.value != confirmPassword.value -> {
                        errorMessage.value = "Passwords do not match"
                    }
                    else -> {
                        errorMessage.value = ""
                        auth.createUserWithEmailAndPassword(email.value, password.value)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                                    // Navigate to LoginActivity
                                    context.startActivity(Intent(context, LoginActivity::class.java))
                                } else {
                                    Toast.makeText(context, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
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

            
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    AppMusicTheme {
        RegisterScreen(FirebaseAuth.getInstance())
    }
}



