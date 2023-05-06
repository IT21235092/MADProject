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




    class EmployeeDetailsActivity : AppCompatActivity() {

        private lateinit var payID: TextView
        private lateinit var paymentAmount: TextView
        private lateinit var cardNumber: TextView
        private lateinit var expirationYear: TextView
        private lateinit var expirationMonth: TextView
        private lateinit var btnUpdate: Button
        private lateinit var btnDelete: Button


        override fun onCreate(savedInstanceState: Bundle?) {

            val actionBar: ActionBar? = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true)
            }

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_employee_details)

            initView()
            setValuesToViews()

            btnUpdate.setOnClickListener {
                openUpdateDialog(
                    intent.getStringExtra("Payment ID").toString(),
                    intent.getStringExtra("Payment Amount").toString()
                )
            }

            btnDelete.setOnClickListener {
                deleteRecord(
                    intent.getStringExtra("Payment ID").toString()
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
            payID = findViewById(R.id.payID)
            paymentAmount = findViewById(R.id.paymentAmount)
            cardNumber = findViewById(R.id.cardNumber)
            expirationYear = findViewById(R.id.expirationYear)
            expirationMonth = findViewById(R.id.expirationMonth)

            btnUpdate = findViewById(R.id.btnUpdate)
            btnDelete = findViewById(R.id.btnDelete)
        }

        private fun setValuesToViews() {
            payID.text = intent.getStringExtra("Payment ID")
            paymentAmount.text = intent.getStringExtra("Payment Amount")
            cardNumber.text = intent.getStringExtra("Card Number")
            expirationYear.text = intent.getStringExtra("Expiration Year")
            expirationMonth.text = intent.getStringExtra("Expiration Month")

        }

        private fun deleteRecord(
            id: String
        ) {
            val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
            val mTask = dbRef.removeValue()

            mTask.addOnSuccessListener {
                Toast.makeText(this, "Employee data deleted", Toast.LENGTH_LONG).show()

                val intent = Intent(this, FetchingActivity::class.java)
                finish()
                startActivity(intent)
            }.addOnFailureListener { error ->
                Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
            }
        }

        private fun openUpdateDialog(
            empId: String,
            empName: String
        ) {
            val mDialog = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val mDialogView = inflater.inflate(R.layout.update_dialog, null)

            mDialog.setView(mDialogView)

            val paymentAmount = mDialogView.findViewById<EditText>(R.id.etpaymentAmount)
            val cardNumber = mDialogView.findViewById<EditText>(R.id.etcardNumber)
            val expirationYear = mDialogView.findViewById<EditText>(R.id.etexpirationYear)
            val expirationMonth = mDialogView.findViewById<EditText>(R.id.etexpirationMonth)

            val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

            paymentAmount.setText(intent.getStringExtra("Payment Amount").toString())
            cardNumber.setText(intent.getStringExtra("Card Number").toString())
            expirationYear.setText(intent.getStringExtra("Expiration Year").toString())
            expirationMonth.setText(intent.getStringExtra("Expiration Month").toString())

            mDialog.setTitle("Updating $empName Record")

            val alertDialog = mDialog.create()
            alertDialog.show()

            btnUpdateData.setOnClickListener {
                updateEmpData(
                    empId,
                    paymentAmount.text.toString(),
                    cardNumber.text.toString(),
                    expirationYear.text.toString(),
                    expirationMonth.text.toString(),
                )

                Toast.makeText(applicationContext, "Employee Data Updated", Toast.LENGTH_LONG)
                    .show()

                //we are setting updated data to our textviews
                paymentAmount.setText(paymentAmount.text.toString())
                cardNumber.setText(cardNumber.text.toString())
                expirationYear.setText(expirationYear.text.toString())
                expirationMonth.setText(expirationMonth.text.toString())

                alertDialog.dismiss()
            }
        }


        private fun updateEmpData(
            id: String,
            name: String,
            age: String,
            year: String,
            month: String
        ) {
            val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
            val empInfo = PaaymentModel(id, name, age, year,month)
            dbRef.setValue(empInfo)
        }

    }
