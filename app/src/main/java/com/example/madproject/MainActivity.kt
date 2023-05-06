package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.madproject.FetchingActivity
import com.example.madproject.InsertionActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        tvUserName = findViewById(R.id.tv_user_name)
        btnInsertData = findViewById(R.id.btnInsertData)
        btnFetchData = findViewById(R.id.btnFetchData)

        // Set the user's phone in the TextView
        val currentUser = auth.currentUser
        val uid = currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
        usersRef.child(uid!!).get().addOnSuccessListener {
            if (it != null) {
                val first = it.child("first").value.toString()
                tvUserName.text = first
            }
        }

        // Set click listeners for the buttons
        btnInsertData.setOnClickListener {
            val intent = Intent(this, InsertionActivity::class.java)
            startActivity(intent)
        }

        btnFetchData.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }
    }

}
