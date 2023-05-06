package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase




class JobDetailsActivity : AppCompatActivity() {

    private lateinit var jobID: TextView
    private lateinit var payment: TextView
    private lateinit var jobType: TextView
    private lateinit var qualification: TextView
    private lateinit var experience: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("Job ID").toString(),
                intent.getStringExtra("Expiration Year").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("Job ID").toString()
            )
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        jobID = findViewById(R.id.payID)
        payment = findViewById(R.id.paymentAmount)
        jobType = findViewById(R.id.cardNumber)
        qualification = findViewById(R.id.expirationYear)
        experience = findViewById(R.id.expirationMonth)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        jobID.text = intent.getStringExtra("Job ID")
        payment.text = intent.getStringExtra("Expiration Year")
        jobType.text = intent.getStringExtra("Job Type")
        qualification.text = intent.getStringExtra("Job Qualification")
        experience.text = intent.getStringExtra("Experirnce")

    }

    private fun deleteRecord(
        id: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Jobs").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Job data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, DisplayJobs::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        jobId: String,
        empName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_job, null)

        mDialog.setView(mDialogView)

        val  payment = mDialogView.findViewById<EditText>(R.id.etpaymentAmount)
        val  jobType = mDialogView.findViewById<EditText>(R.id.etcardNumber)
        val qualification = mDialogView.findViewById<EditText>(R.id.etexpirationYear)
        val experience = mDialogView.findViewById<EditText>(R.id.etexpirationMonth)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        payment.setText(intent.getStringExtra("Expiration Year").toString())
        jobType.setText(intent.getStringExtra("Job Type").toString())
        qualification.setText(intent.getStringExtra("Job Qualification").toString())
        experience.setText(intent.getStringExtra("Experirnce").toString())

        mDialog.setTitle("Updating $empName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                jobId,
                payment.text.toString(),
                jobType.text.toString(),
                qualification.text.toString(),
                experience.text.toString(),
            )

            Toast.makeText(applicationContext, "Job Data Updated", Toast.LENGTH_LONG)
                .show()

            //we are setting updated data to our textviews
            payment.setText(payment.text.toString())
            jobType.setText(jobType.text.toString())
            qualification.setText(qualification.text.toString())
            experience.setText(experience.text.toString())

            alertDialog.dismiss()
        }
    }


    private fun updateEmpData(
        id: String,
        pay: String,
        job: String,
        hours: String,
        method: String,

    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Jobs").child(id)

        val empUpdates = HashMap<String, Any>()
        empUpdates["payment"] = pay
        empUpdates["jobType"] = job
        empUpdates["qualification"] = hours
        empUpdates["experience"] = method


        dbRef.updateChildren(empUpdates).addOnSuccessListener {
            jobID.text = id
            payment.text = pay
            jobType.text = job
            qualification.text = hours
            experience.text = method

        }
    }
}
