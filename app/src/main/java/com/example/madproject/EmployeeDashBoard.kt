package com.example.madproject
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class EmployeeDashBoard: AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var createJob: ImageView
    private lateinit var home: ImageView
    private lateinit var displayJob: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserListener: ValueEventListener
    private lateinit var logout: ImageView
    private lateinit var profile: ImageView
    private lateinit var feedback: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_dashboard)

        auth = Firebase.auth
        tvUserName = findViewById(R.id.textViewName1)
        createJob = findViewById(R.id.imageView31)
        home = findViewById(R.id.imageView3)
        displayJob = findViewById(R.id.imageView33)
        profile = findViewById(R.id.imageView36)
        feedback = findViewById(R.id.imageView37)

        // Set the user's phone in the TextView
        val currentUser = auth.currentUser
        val uid = currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
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
        createJob.setOnClickListener {
            val intent = Intent(this, CreateJob::class.java)
            intent.putExtra("first", tvUserName.text.toString())
            startActivity(intent)
        }
        displayJob.setOnClickListener {
            val intent = Intent(this, DisplayJobs::class.java)
            startActivity(intent)
        }

        home.setOnClickListener {
            val intent = Intent(this, JobHome::class.java)
            startActivity(intent)
        }
        profile.setOnClickListener{
            val intent = Intent(this, CustomerViewProfileActivity::class.java)
            startActivity(intent)
        }
        feedback.setOnClickListener{
            val intent = Intent(this, FeedFetchingEmployee::class.java)
            startActivity(intent)
        }
        logout = findViewById(R.id.imageView35)
        logout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}