package com.example.madproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class CustomerEditProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var firstEditText: EditText
    private lateinit var lastEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var nicEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_customer_profile)

        auth = Firebase.auth

        firstEditText = findViewById(R.id.first_edittext)
        lastEditText = findViewById(R.id.last_edittext)
        emailEditText = findViewById(R.id.email_edittext)
        nicEditText = findViewById(R.id.nic_edittext)
        addressEditText = findViewById(R.id.address_edittext)
        phoneEditText = findViewById(R.id.phone_edittext)
        saveButton = findViewById(R.id.save_button)
        deleteButton = findViewById(R.id.delete_button)

        // Load user data
        val user = auth.currentUser
        val uid = user?.uid
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
        uid?.let {
            usersRef.child(it).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val userData = snapshot.value as HashMap<*, *>
                    firstEditText.setText(userData["first"].toString())
                    lastEditText.setText(userData["last"].toString())
                    emailEditText.setText(userData["email"].toString())
                    addressEditText.setText(userData["address"].toString())
                    phoneEditText.setText(userData["phone"].toString())
                    nicEditText.setText(userData["nic"].toString())
                }
            }
        }

        deleteButton.setOnClickListener {
            // Clear all edit boxes
            firstEditText.text.clear()
            lastEditText.text.clear()
            emailEditText.text.clear()
            addressEditText.text.clear()
            phoneEditText.text.clear()
            nicEditText.text.clear()
        }

        saveButton.setOnClickListener {
            val first = firstEditText.text.toString()
            val last = lastEditText.text.toString()
            val email = emailEditText.text.toString()
            val address = addressEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val nic = nicEditText.text.toString()

            // Update user data in Firebase Realtime Database
            uid?.let {
                val userData = HashMap<String, String>()
                userData["first"] = first
                userData["last"] = last
                userData["email"] = email
                userData["address"] = address
                userData["phone"] = phone
                userData["nic"] = nic

                usersRef.child(it).updateChildren(userData as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(
                            baseContext, "Profile updated successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            baseContext, "Failed to update profile.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }
}
