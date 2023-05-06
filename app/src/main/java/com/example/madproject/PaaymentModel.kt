package com.example.madproject

data class PaaymentModel(
    var empId: String? = null,
    var paymentAmount: String? = null,
    var cardNumber: String? = null,
    var expirationMonth: String? = null,
    var expirationYear: String? = null,
    var cvv: String? = null,
    var paymentPerHour: String? = null,
    var paymentMethod: String? = null,
    var payer: String? = null,
    var jobType: String? = null
)