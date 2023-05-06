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


    class PaymentDetailsActivity : AppCompatActivity() {

        private lateinit var payID: TextView
        private lateinit var payer: TextView
        private lateinit var jobType: TextView
        private lateinit var paymentPerHour: TextView
        private lateinit var paymentMethod: TextView
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

        override fun onResume() {
            super.onResume()
            // Update the views with the latest values
            setValuesToViews()
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
            payer = findViewById(R.id.payer)
            jobType = findViewById(R.id.jobType)
            paymentPerHour = findViewById(R.id.paymentPerHour)
            paymentMethod = findViewById(R.id.paymentMethod)
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
            payer.text = intent.getStringExtra("Payer")
            jobType.text = intent.getStringExtra("Job Type")
            paymentPerHour.text = intent.getStringExtra("Payment Per Hour")
            paymentMethod.text = intent.getStringExtra("Payment Method")
            cardNumber.text = intent.getStringExtra("Card Number")
            expirationYear.text = intent.getStringExtra("Expiration Year")
            expirationMonth.text = intent.getStringExtra("Expiration Month")
            paymentAmount.text = intent.getStringExtra("Payment Amount")

        }

        private fun deleteRecord(
            id: String
        ) {
            val dbRef = FirebaseDatabase.getInstance().getReference("Payment").child(id)
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
            PayAmount: String
        ) {
            val mDialog = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val mDialogView = inflater.inflate(R.layout.update_dialog, null)

            mDialog.setView(mDialogView)


            val payer = mDialogView.findViewById<EditText>(R.id.etpayer)
            val jobType = mDialogView.findViewById<EditText>(R.id.etjobType)
            val paymentPerHour = mDialogView.findViewById<EditText>(R.id.etPaymentPerHour)
            val paymentMethod = mDialogView.findViewById<EditText>(R.id.etPaymentMethod)
            val cardNumber = mDialogView.findViewById<EditText>(R.id.etcardNumber)
            val paymentAmount = mDialogView.findViewById<EditText>(R.id.etpaymentAmount)

            val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)


            payer.setText(intent.getStringExtra("Payer").toString())
            jobType.setText(intent.getStringExtra("Job Type").toString())
            paymentPerHour.setText(intent.getStringExtra("Payment Per Hour").toString())
            paymentMethod.setText(intent.getStringExtra("Payment Method").toString())
            cardNumber.setText(intent.getStringExtra("Card Number").toString())
            paymentAmount.setText(intent.getStringExtra("Payment Amount").toString())

            mDialog.setTitle("Updating $PayAmount Record")

            val alertDialog = mDialog.create()
            alertDialog.show()

            btnUpdateData.setOnClickListener {
                updateEmpData(
                    empId,
                    payer.text.toString(),
                    jobType.text.toString(),
                    paymentPerHour.text.toString(),
                    paymentMethod.text.toString(),
                    cardNumber.text.toString(),
                    paymentAmount.text.toString(),
                )

                Toast.makeText(applicationContext, "Payment Data Updated", Toast.LENGTH_LONG)
                    .show()

                //we are setting updated data to our textviews
                payer.setText(payer.text.toString())
                jobType.setText(jobType.text.toString())
                paymentPerHour.setText(paymentPerHour.text.toString())
                paymentMethod.setText(paymentMethod.text.toString())
                cardNumber.setText(cardNumber.text.toString())
                paymentAmount.setText(paymentAmount.text.toString())

                alertDialog.dismiss()
            }
        }


        private fun updateEmpData(
            id: String,
            pay: String,
            job: String,
            hours: String,
            method: String,
            age: String,
            name: String,
        ) {
            val dbRef = FirebaseDatabase.getInstance().getReference("Payment").child(id)

            val empUpdates = HashMap<String, Any>()
            empUpdates["payer"] = pay
            empUpdates["jobType"] = job
            empUpdates["paymentPerHour"] = hours
            empUpdates["paymentMethod"] = method
            empUpdates["cardNumber"] = age
            empUpdates["paymentAmount"] = name

            dbRef.updateChildren(empUpdates).addOnSuccessListener {
                payID.text = id
                payer.text = pay
                jobType.text = job
                paymentPerHour.text = hours
                paymentMethod.text = method
                cardNumber.text = age
                paymentAmount.text = name
            }
        }



    }
