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

class CustomerFetchingActivity : AppCompatActivity() {

    private lateinit var payRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var payList: ArrayList<PaaymentModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        payRecyclerView = findViewById(R.id.rvEmp)
        payRecyclerView.layoutManager = LinearLayoutManager(this)
        payRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        payList = arrayListOf<PaaymentModel>()

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

        payRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid
        val database = FirebaseDatabase.getInstance()
        var usersRef = database.getReference("users")

        usersRef.child(uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val first = snapshot.child("first").value.toString()

                    dbRef = FirebaseDatabase.getInstance().getReference("Payment")
                    dbRef.orderByChild("payer").equalTo(first)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                payList.clear()
                                if (snapshot.exists()) {
                                    for (empSnap in snapshot.children) {
                                        val empData = empSnap.getValue(PaaymentModel::class.java)
                                        payList.add(empData!!)
                                    }
                                    val mAdapter = CustAdapter(payList)
                                    payRecyclerView.adapter = mAdapter

                                    mAdapter.setOnItemClickListener(object :
                                        CustAdapter.onItemClickListener {
                                        override fun onItemClick(position: Int) {

                                            val intent = Intent(
                                                this@CustomerFetchingActivity,
                                                PaymentDetailsActivity::class.java
                                            )

                                            //put extras
                                            intent.putExtra("Payment ID", payList[position].empId)
                                            intent.putExtra(
                                                "Payment Amount",
                                                payList[position].paymentAmount
                                            )
                                            intent.putExtra(
                                                "Card Number",
                                                payList[position].cardNumber
                                            )
                                            intent.putExtra(
                                                "Expiration Year",
                                                payList[position].expirationYear
                                            )
                                            intent.putExtra(
                                                "Expiration Month",
                                                payList[position].expirationMonth
                                            )
                                            intent.putExtra(
                                                "Payment Per Hour",
                                                payList[position].paymentPerHour
                                            )
//                            intent.putExtra(
//                                "Payment Method",
//                                payList[position].paymentMethod
//                            )
                                            intent.putExtra(
                                                "Card Number",
                                                payList[position].cardNumber
                                            )
                                            intent.putExtra(
                                                "Expiration Year",
                                                payList[position].expirationYear
                                            )
                                            intent.putExtra(
                                                "Expiration Month",
                                                payList[position].expirationMonth
                                            )
                                            intent.putExtra(
                                                "Payment Amount",
                                                payList[position].paymentAmount
                                            )
                                            startActivity(intent)
                                        }

                                    })

                                    payRecyclerView.visibility = View.VISIBLE
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

