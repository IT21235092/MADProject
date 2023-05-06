package com.example.madproject

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var payer: EditText
    private lateinit var paymentAmount: EditText
    private lateinit var cardNumber: EditText
    private lateinit var expirationMonth: EditText
    private lateinit var expirationYear: EditText
    private lateinit var cvv: EditText
    private lateinit var paymentPerHour: EditText
    private lateinit var payButton: Button
    private lateinit var paymentMethodRadioGroup: RadioGroup
    private var paymentMethod: String = ""
    private var  jobType: String = ""




    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment)


        jobType = intent.getStringExtra("jobType").toString() // Retrieve jobType from the intent

        // ...

        // Set the jobType value in the EditText
        val jobTypeEditText: EditText = findViewById(R.id.jobTypeEditText)
        jobTypeEditText.setText(jobType)


        paymentMethodRadioGroup = findViewById(R.id.paymentMethodRadioGroup)
        paymentMethodRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.visaRadioButton -> paymentMethod = "Visa"
                R.id.creditRadioButton -> paymentMethod = "Credit"
                R.id.debitRadioButton -> paymentMethod = "Debit"
            }
        }

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        payer = findViewById(R.id.etpayer)
        val username = intent.getStringExtra("first")
        if (username != null && username.isNotEmpty()) {
            payer.setText(username)
        }



        paymentAmount = findViewById(R.id.etpaymentAmount)
        cardNumber = findViewById(R.id.etcardNumber)
        expirationMonth = findViewById(R.id.etexpirationMonth)
        expirationYear = findViewById(R.id.etexpirationYear)
        cvv = findViewById(R.id.etcvv)
        paymentPerHour = findViewById(R.id.etPaymentPerHour)
        payButton = findViewById(R.id.etpayButton)


        dbRef = FirebaseDatabase.getInstance().getReference("Payment")

        paymentPerHour.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculatePaymentAmount()
            }
        })

        payButton.setOnClickListener {
            savePaymentData()
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

    private fun isCreditCardNumberValid(cardNumber: String): Boolean {
        val regex = "^[0-9]{16}\$".toRegex() // regular expression for normal credit card number
        return regex.matches(cardNumber)
    }





    private fun savePaymentData() {


        //getting values

        val payerString = payer.text.toString()
        val paymentAmountString = paymentAmount.text.toString()
        val cardNumberString = cardNumber.text.toString()
        val expirationMonthString = expirationMonth.text.toString()
        val expirationYearString = expirationYear.text.toString()
        val cvvString = cvv.text.toString()
        val paymentPerHourString = paymentPerHour.text.toString()

        if (payerString.isEmpty()) {
            paymentAmount.error = "Please enter name"
            return
        }
        if (paymentAmountString.isEmpty()) {
            paymentAmount.error = "Please enter payment amount"
            return
        }
        if (cardNumberString.isEmpty()) {
            cardNumber.error = "Please enter Valid Card Number"
            return
        }
        if (!isCreditCardNumberValid(cardNumberString)) {
            cardNumber.error = "Please enter valid credit card number"
            return
        }
        if (expirationMonthString.isEmpty()) {
            expirationMonth.error = "Please enter valid expiration month"
            return
        }
        if (expirationYearString.isEmpty()) {
            expirationYear.error = "Please enter valid expiration year"
            return
        }
        if (cvvString.isEmpty()) {
            cvv.error = "Please enter valid cvv"
            return
        }
        if (paymentPerHourString.isEmpty()) {
            paymentPerHour.error = "Please enter valid payment per hour"
            return
        }

        val empId = dbRef.push().key!!


        val employee = PaaymentModel(
            empId,
            paymentAmountString,
            cardNumberString,
            expirationMonthString,
            expirationYearString,
            cvvString,
            paymentPerHourString,
            paymentMethod,
            payerString,
            jobType
        )

        dbRef.child(empId).setValue(employee).addOnCompleteListener {
            Toast.makeText(
                applicationContext,
                "Payment data saved successfully",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun calculatePaymentAmount() {
        val paymentPerHourString = paymentPerHour.text.toString()
        val paymentString = intent.getStringExtra("payment")

        if (paymentPerHourString.isNotEmpty() && paymentString != null && paymentString.isNotEmpty()) {
            val paymentPerHourDouble = paymentPerHourString.toDouble()
            val paymentDouble = paymentString.toDouble()
            val paymentAmountDouble = paymentPerHourDouble * paymentDouble
            val paymentAmountString = paymentAmountDouble.toString()
            paymentAmount.setText(paymentAmountString)
        }
    }

}