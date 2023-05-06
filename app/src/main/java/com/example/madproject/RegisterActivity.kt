package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var firstEditText: EditText
    private lateinit var lastEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var nicEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var custReg: Switch


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val LoginBtn = findViewById<Button>(R.id.logbtn)
        LoginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        custReg = findViewById(R.id.switch6)
        custReg.setOnClickListener {
            val intent = Intent(this, CustomerRegisterActivity::class.java)
            startActivity(intent)
        }

        auth = Firebase.auth

        firstEditText = findViewById(R.id.first_edittext)
        lastEditText = findViewById(R.id.last_edittext)
        emailEditText = findViewById(R.id.email_edittext)
        nicEditText = findViewById(R.id.nic_edittext)
        passwordEditText = findViewById(R.id.password_edittext)
        addressEditText = findViewById(R.id.address_edittext)
        phoneEditText = findViewById(R.id.phone_edittext)
        registerButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            val first = firstEditText.text.toString()
            val last = lastEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val address = addressEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val nic = nicEditText.text.toString()

            registerUser(first,last,email, password, address, phone, nic)
        }
    }



    private fun registerUser(first: String, last: String, email: String, password: String, address: String, phone: String, nic: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Save user details to Firebase Realtime Database
                    val user = auth.currentUser
                    val uid = user?.uid
                    val database = FirebaseDatabase.getInstance()
                    val usersRef = database.getReference("users")

                    val userData = HashMap<String, String>()
                    userData["first"] = first
                    userData["last"] = last
                    userData["email"] = email
                    userData["address"] = address
                    userData["phone"] = phone
                    userData["nic"] = nic

                    uid?.let {
                        usersRef.child(it).setValue(userData)
                    }

                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this, EmployeeDashBoard::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }
}
