package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class FeedFetchingEmployee : AppCompatActivity() {
    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView

    private lateinit var feedList: ArrayList<FeedbackModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var tvUserName: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_fetching)

        auth = Firebase.auth

        feedRecyclerView = findViewById(R.id.rvTask)

        feedRecyclerView.layoutManager = LinearLayoutManager(this)
        feedRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)
        tvUserName = findViewById(R.id.tv_user_name)




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

        feedList = arrayListOf<FeedbackModel>()

        getFeedBackData()

    }

    private fun getFeedBackData() {
        feedRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE
        dbRef = FirebaseDatabase.getInstance().getReference("FeedBack")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                feedList.clear()
                if (snapshot.exists()) {
                    val currentUser = tvUserName.text.toString()
                    for (taskSnap in snapshot.children) {
                        val taskData = taskSnap.getValue(FeedbackModel::class.java)
                        if (taskData?.employee == currentUser) {
                            feedList.add(taskData!!)
                        }
                    }
                    val mAdapter = FeedAdapter(feedList)
                    feedRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : FeedAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(
                                this@FeedFetchingEmployee,
                                FeedBackDetailsActivity::class.java
                            )

                            //put extra
                            intent.putExtra("empId", feedList[position].empId)
                            intent.putExtra("empFullName", feedList[position].empFullName)
                            intent.putExtra("empAddtional", feedList[position].empAddtional)
                            intent.putExtra("employee", feedList[position].employee)
                            startActivity(intent)
                        }
                    })

                    feedRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                } else {
                    // Handle case when there is no data available
                    feedRecyclerView.visibility = View.GONE
                    tvLoadingData.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }


}