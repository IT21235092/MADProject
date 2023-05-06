package com.example.madproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class CustomerViewProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var firstEditText: TextView
    private lateinit var lastEditText: TextView
    private lateinit var emailEditText: TextView
    private lateinit var addressEditText: TextView
    private lateinit var phoneEditText: TextView
    private lateinit var nicEditText: TextView
    private lateinit var editButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_customer_profile)

        auth = Firebase.auth

        firstEditText = findViewById(R.id.first_edittext)
        lastEditText = findViewById(R.id.last_edittext)
        emailEditText = findViewById(R.id.email_edittext)
        nicEditText = findViewById(R.id.nic_edittext)
        addressEditText = findViewById(R.id.address_edittext)
        phoneEditText = findViewById(R.id.phone_edittext)
        editButton = findViewById(R.id.edit_button)

        editButton.setOnClickListener {
            val intent = Intent(this, CustomerEditProfileActivity::class.java)
            startActivity(intent)
        }

        // load user data
        loadUserData()
    }

    override fun onResume() {
        super.onResume()

        loadUserData()
    }

    private fun loadUserData() {
        val user = auth.currentUser
        val uid = user?.uid
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
        uid?.let {
            usersRef.child(it).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val userData = snapshot.value as HashMap<*, *>
                    firstEditText.text = userData["first"].toString()
                    lastEditText.text = userData["last"].toString()
                    emailEditText.text = userData["email"].toString()
                    addressEditText.text = userData["address"].toString()
                    phoneEditText.text = userData["phone"].toString()
                    nicEditText.text = userData["nic"].toString()
                }
            }
        }
    }
}
