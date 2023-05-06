package com.example.madproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class JobHome : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var usersRef: DatabaseReference
    private lateinit var itButton: ImageView
    private lateinit var healthButton: ImageView
    private lateinit var graphicButton: ImageView
    private lateinit var mechanicButton: ImageView
    private lateinit var technicalButton: ImageView
    private lateinit var educateButton: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var currentUserListener: ValueEventListener
    private lateinit var back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth
        val currentUser = auth.currentUser
        val uid = currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_vacancy)

        tvUserName = findViewById(R.id.tv_user_name)

        // Set the user's name in the TextView
        currentUserListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val first = snapshot.child("first").value.toString()
                    tvUserName.text = first
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        usersRef.child(uid!!).addValueEventListener(currentUserListener)

        itButton= findViewById(R.id.it1)
        itButton.setOnClickListener {
            val intent = Intent(this, JobIT::class.java)
            startActivity(intent)


        }

        healthButton= findViewById(R.id.hlth1)
        healthButton.setOnClickListener {
            val intent = Intent(this, JobHealth::class.java)
            startActivity(intent)


        }

        technicalButton= findViewById(R.id.techni1)
        technicalButton.setOnClickListener {
            val intent = Intent(this, JobTechnical::class.java)
            startActivity(intent)


        }
        mechanicButton= findViewById(R.id.mech1)
        mechanicButton.setOnClickListener {
            val intent = Intent(this, JobMechanic::class.java)
            startActivity(intent)


        }
        graphicButton= findViewById(R.id.grphic1)
        graphicButton.setOnClickListener {
            val intent = Intent(this, JobGraphic::class.java)
            startActivity(intent)


        }
        educateButton= findViewById(R.id.edu1)
        educateButton.setOnClickListener {
            val intent = Intent(this, JobEducate::class.java)
            startActivity(intent)




        }

        back= findViewById(R.id.back1)
        back.setOnClickListener {
            val intent = Intent(this, EmployeeDashBoard::class.java)
            startActivity(intent)




        }
    }
}
