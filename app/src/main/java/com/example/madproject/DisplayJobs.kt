package com.example.madproject

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DisplayJobs : AppCompatActivity() {

    private lateinit var jobRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var jobList: ArrayList<JobModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_jobs)

        jobRecyclerView = findViewById(R.id.rvEmp)
        jobRecyclerView.layoutManager = LinearLayoutManager(this)
        jobRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        jobList = arrayListOf<JobModel>()

        getEmployeesData()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getEmployeesData() {

        jobRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        var usersRef = database.getReference("users")

        usersRef.child(uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val first = snapshot.child("first").value.toString()

                    dbRef = FirebaseDatabase.getInstance().getReference("Jobs")
                    dbRef.orderByChild("payer").equalTo(first)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                jobList.clear()
                                if (snapshot.exists()) {
                                    for (empSnap in snapshot.children) {
                                        val empData = empSnap.getValue(JobModel::class.java)
                                        jobList.add(empData!!)
                                    }
                                    val mAdapter = JobAdapter(jobList)
                                    jobRecyclerView.adapter = mAdapter

                                    mAdapter.setOnItemClickListener(object :
                                        JobAdapter.onItemClickListener {
                                        override fun onItemClick(position: Int) {

                                            val intent = Intent(
                                                this@DisplayJobs,
                                                JobDetailsActivity::class.java
                                            )

                                            //put extras
                                            intent.putExtra("Job ID", jobList[position].jobId)
                                            intent.putExtra("Job Qualification", jobList[position].qualification)
                                            intent.putExtra("Experirnce", jobList[position].experience)
                                            intent.putExtra("Expiration Year", jobList[position].payment)
                                            intent.putExtra("Job Type", jobList[position].jobType)
                                            startActivity(intent)
                                        }

                                    })

                                    jobRecyclerView.visibility = View.VISIBLE
                                    tvLoadingData.visibility = View.GONE
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}