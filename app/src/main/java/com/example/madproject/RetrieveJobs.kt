package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class RetrieveJobs : AppCompatActivity() {

    private lateinit var jobIdTextView: TextView
    private lateinit var payerTextView: TextView
    private lateinit var jobTypeTextView: TextView
    private lateinit var jobCategoryTextView: TextView
    private lateinit var paymentTextView: TextView
    private lateinit var qualificationTextView: TextView
    private lateinit var experienceTextView: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var usersRef: DatabaseReference
    private lateinit var currentUserListener: ValueEventListener
    private lateinit var tvUserName: TextView
    private lateinit var btnInsertData: Button
    private lateinit var btnInsertDataF: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrieve_jobs)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        val uid = currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")



        payerTextView = findViewById(R.id.payerTextView)
        jobTypeTextView = findViewById(R.id.jobTypeTextView)
        jobCategoryTextView = findViewById(R.id.jobCategoryTextView)
        paymentTextView = findViewById(R.id.paymentTextView)
        qualificationTextView = findViewById(R.id.qualificationTextView)
        experienceTextView = findViewById(R.id.experienceTextView)
        tvUserName = findViewById(R.id.tv_user_name)
        btnInsertData = findViewById(R.id.btnInsertData)
        btnInsertDataF = findViewById(R.id.btnFeedback)
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

        btnInsertDataF.setOnClickListener {
            val intent = Intent(this, FeedInsertionActivity::class.java)
            intent.putExtra("first", tvUserName.text.toString())
            intent.putExtra("payer", payerTextView.text.toString())
            startActivity(intent)
        }
        btnInsertData.setOnClickListener {
            val intent = Intent(this, InsertionActivity::class.java)
            intent.putExtra("first", tvUserName.text.toString())
            intent.putExtra("payment", paymentTextView.text.toString())
            intent.putExtra("jobType", jobTypeTextView.text.toString())
            startActivity(intent)
        }

        // Get the jobId passed from JobIT activity
        val jobId = intent.getStringExtra("jobId")

        // Get a reference to the job with the given jobId
        val jobRef = FirebaseDatabase.getInstance().getReference("Jobs").child(jobId.toString())

        // Attach a listener to retrieve the job data
        jobRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val payer = snapshot.child("payer").getValue(String::class.java)
                val jobType = snapshot.child("jobType").getValue(String::class.java)
                val jobCategory = snapshot.child("jobCategory").getValue(String::class.java)
                val qualification = snapshot.child("qualification").getValue(String::class.java)
                val experience = snapshot.child("experience").getValue(String::class.java)
                val payment = snapshot.child("payment").getValue(String::class.java)

                // Set the job data to the text views

                payerTextView.text = payer
                jobTypeTextView.text = jobType
                jobCategoryTextView.text = jobCategory
                paymentTextView.text = payment
                qualificationTextView.text = qualification
                experienceTextView.text = experience

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
            }
        })
    }
}
