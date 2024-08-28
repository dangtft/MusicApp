package com.example.appmusic.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.appmusic.R
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : ComponentActivity(){

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Retrieve user information from Firebase Authentication
            val email = currentUser.email
            val name = currentUser.displayName
            val phone = currentUser.phoneNumber

                // Update UI elements
                findViewById<TextView>(R.id.mail_user).text = email
                findViewById<TextView>(R.id.phone_user).text = phone ?: "Phone number not available"
                findViewById<TextView>(R.id.name_user).text = name ?: "Name not available"
            }
        findViewById<Button>(R.id.changePass).setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
