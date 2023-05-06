package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class JobGraphic : AppCompatActivity() {

    private lateinit var jobRecyclerView: RecyclerView
    private lateinit var jobAdapter: JobAdapter

    interface OnItemClickListener {
        fun onItemClick(job: Job)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_it)

        jobRecyclerView = findViewById(R.id.jobRecyclerView)
        jobAdapter = JobAdapter()

        jobRecyclerView.adapter = jobAdapter
        jobRecyclerView.layoutManager = LinearLayoutManager(this)

        val jobsRef = FirebaseDatabase.getInstance().getReference("Jobs")

        jobsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jobAdapter.clear()
                for (jobSnapshot in snapshot.children) {
                    val jobCategory = jobSnapshot.child("jobCategory").getValue(String::class.java)
                    val payer = jobSnapshot.child("payer").getValue(String::class.java)
                    val jobType = jobSnapshot.child("jobType").getValue(String::class.java)
                    val payment = jobSnapshot.child("payment").getValue(String::class.java)

                    if (jobCategory == "Graphic") {
                        jobAdapter.add(Job(payer, jobType, payment, jobSnapshot.key))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
            }
        })

        jobAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(job: Job) {
                val intent = Intent(this@JobGraphic, RetrieveJobs::class.java)
                intent.putExtra("jobId", job.id)
                startActivity(intent)
            }
        })
    }


    // Define a data class to represent a job
    data class Job(val payer: String?, val jobType: String?, val payment: String?, val id: String?)

    // Define a RecyclerView adapter to display the job info
    inner class JobAdapter : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {
        private val jobList = mutableListOf<Job>()
        private var listener: OnItemClickListener? = null

        fun setOnItemClickListener(listener: OnItemClickListener) {
            this.listener = listener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.job_it, parent, false)
            return JobViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
            val currentJob = jobList[position]
            holder.payerTextView.text = currentJob.payer
            holder.jobTypeTextView.text = currentJob.jobType
            holder.paymentTextView.text = currentJob.payment

            holder.itemView.setOnClickListener {
                listener?.onItemClick(currentJob)
            }
        }

        override fun getItemCount() = jobList.size

        fun add(job: Job) {
            jobList.add(job)
            notifyDataSetChanged()
        }

        fun clear() {
            jobList.clear()
            notifyDataSetChanged()
        }

        inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val payerTextView: TextView = itemView.findViewById(R.id.payerTextView)
            val jobTypeTextView: TextView = itemView.findViewById(R.id.jobTypeTextView)
            val paymentTextView: TextView = itemView.findViewById(R.id.typeTextView)
        }
    }
}
