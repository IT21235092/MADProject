package com.example.madproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.madproject.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FeedInsertionActivity : AppCompatActivity() {
    private lateinit var etFullName: EditText
    private lateinit var etAdditional: EditText
    private lateinit var btnSave: Button
    private lateinit var dbRef: DatabaseReference
    private var employee: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_insertion)

        etFullName = findViewById(R.id.etFullName)
        etAdditional = findViewById(R.id.etAdditional)
        btnSave = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("FeedBack")

        val tvUserName = intent.getStringExtra("first")
        etFullName.setText(tvUserName)

        btnSave.setOnClickListener {
            saveTaskData()
            val intent = Intent(this, FeedFetchingActivity::class.java)
            startActivity(intent)
        }

        employee = intent.getStringExtra("payer").toString()
        val emploeeEditText: TextView = findViewById(R.id.payer1)
        emploeeEditText.setText(employee)
    }

    private fun saveTaskData() {
        //getting values
        val empFullName = etFullName.text.toString()
        val empAdditional = etAdditional.text.toString()

        if (empFullName.isEmpty()) {
            etFullName.error = "Please enter Feedback Name"
            return
        }

        if (empAdditional.isEmpty()) {
            etAdditional.error = "Please enter additional information"
            return
        }

        val empId = dbRef.push().key!!
        val task = FeedbackModel(empId, empFullName, empAdditional, employee)

        dbRef.child(empId).setValue(task)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()
                etFullName.text.clear()
                etAdditional.text.clear()
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}
