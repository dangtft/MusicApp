package com.example.appmusic.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.appmusic.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : ComponentActivity(){

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            // Retrieve user information from Firebase
            database.child("users").child(userId).get().addOnSuccessListener { dataSnapshot ->
                val email = dataSnapshot.child("email").value.toString()
                val name = dataSnapshot.child("name").value.toString()
                val phone = dataSnapshot.child("phone").value.toString()

                // Update UI elements
                findViewById<TextView>(R.id.mail_user).text = email
                findViewById<TextView>(R.id.phone_user).text = phone
                findViewById<TextView>(R.id.name_user).text = name
            }.addOnFailureListener {
                // Handle any errors here
            }
        }
    }
}