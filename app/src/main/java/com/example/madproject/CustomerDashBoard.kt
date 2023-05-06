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


class CustomerDashBoard: AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var logout: ImageView
    private lateinit var displayJob: ImageView
    private lateinit var profile: ImageView
    private lateinit var home: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var feedback: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_dashboard)

        auth = Firebase.auth
        tvUserName = findViewById(R.id.textViewName1)
        displayJob = findViewById(R.id.imageView33)
        profile = findViewById(R.id.imageView36)
        home = findViewById(R.id.imageView3)
        feedback = findViewById(R.id.imageView37)
        logout = findViewById(R.id.imageView35)
        logout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, CusLoginActivity::class.java)
            startActivity(intent)
            finish()

        }


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
//        createJob.setOnClickListener {
//            val intent = Intent(this, CreateJob::class.java)
//            startActivity(intent)
//        }
        displayJob.setOnClickListener {
            val intent = Intent(this, FetchingActivity::class.java)
            startActivity(intent)
        }

        profile.setOnClickListener{
            val intent = Intent(this, CustomerViewProfileActivity::class.java)
            startActivity(intent)
        }
        home.setOnClickListener {
            val intent = Intent(this, CustomerHome::class.java)
            startActivity(intent)
        }
        feedback.setOnClickListener{
            val intent = Intent(this, FeedFetchingActivity::class.java)
            startActivity(intent)
        }
    }
}