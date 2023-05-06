package com.example.madproject

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateJob : AppCompatActivity() {

    private lateinit var payer: EditText
    private lateinit var jobTypeSpinner: Spinner
    private var jobType: String = ""
    private lateinit var jobCategorySpinner: Spinner
    private var jobCategory: String = ""
    private lateinit var qualification: EditText
    private lateinit var experience: EditText
    private lateinit var payment: EditText
    private lateinit var addJob: Button

    private lateinit var dbRef: DatabaseReference

    private val categoryToJobTypeMap = mapOf(
        "Health" to listOf("Nurse", "Doctor", "Medical Technician", "Pharmacist", "Physical Therapist"),
        "IT" to listOf("Software Engineer", "Web Developer", "Data Scientist", "Mobile Developer"),
        "Graphic" to listOf("Graphic Designer", "Animator", "Illustrator"),
        "Technical" to listOf("Mechanical Engineer", "Electrical Engineer", "Civil Engineer"),
        "Mechanic" to listOf("Automotive Mechanic", "Motorcycle Mechanic", "Heavy Equipment Mechanic"),
        "Educating" to listOf("Teacher", "Tutor", "Educational Administrator")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_job)

        payer = findViewById(R.id.etpayer)
        val username = intent.getStringExtra("first")
        if (username != null && username.isNotEmpty()) {
            payer.setText(username)
        }

        qualification = findViewById(R.id.qualifications)
        experience = findViewById(R.id.experience)
        payment = findViewById(R.id.paymentP)
        addJob = findViewById(R.id.addJobBtn)

        dbRef = FirebaseDatabase.getInstance().getReference("Jobs")

        jobCategorySpinner = findViewById(R.id.spinnerCategory)

        val jobCategory = listOf("Health", "IT", "Graphic", "Technical", "Mechanic", "Educating")
        val jobCategoryAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, jobCategory)

        jobCategorySpinner.adapter = jobCategoryAdapter

        jobCategorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedJobCategory = parent.getItemAtPosition(position) as String
                    this@CreateJob.jobCategory = selectedJobCategory
                    val jobTypeList = categoryToJobTypeMap[selectedJobCategory] ?: listOf()
                    jobTypeSpinner.adapter = ArrayAdapter(
                        this@CreateJob,
                        android.R.layout.simple_spinner_dropdown_item,
                        jobTypeList
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Handle no selection
                }
            }

        jobTypeSpinner = findViewById(R.id.spinner)

        jobTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedJobType = parent.getItemAtPosition(position) as String
                    jobType = selectedJobType
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Handle no selection
                }
            }

        addJob.setOnClickListener {
            saveJobData()
        }

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
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

    private fun saveJobData() {

        //getting values
        val payerString = payer.text.toString()
        val qualificationString = qualification.text.toString()
        val experienceString = experience.text.toString()
        val paymentString = payment.text.toString()


        if (qualificationString.isEmpty()) {
            qualification.error = "Please enter Valid experience in text"
            return
        }
        if (experienceString.isEmpty()) {
            experience.error = "Please enter Valid experience"
            return
        }
        if (paymentString.isEmpty()) {
            payment.error = "Please enter valid amount"
            return
        }
        if (payerString.isEmpty()) {
            payer.error = "Please enter valid amount"
            return
        }


        val jobId = dbRef.push().key!!


        val job = JobModel(
            jobId,
            qualificationString,
            experienceString,
            paymentString,
            jobType,
            payerString,
            jobCategory
        )


        dbRef.child(jobId).setValue(job).addOnCompleteListener {
            Toast.makeText(
                applicationContext,
                "Job data saved successfully",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }




}