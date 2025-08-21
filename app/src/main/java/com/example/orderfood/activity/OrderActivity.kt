package com.example.orderfood.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.orderfood.R
import com.example.orderfood.Utils.Utils
import com.example.orderfood.model.OrderModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar

class OrderActivity : AppCompatActivity() {
    private lateinit var edtFullName: EditText
    private lateinit var edtPhoneNumber: EditText
    private lateinit var edtAddress: EditText
    private lateinit var edtMessage: EditText
    private lateinit var spinnerPayMethod: Spinner
    private lateinit var btnOrder: Button
    private lateinit var progressBar : ProgressBar
    private lateinit var txtTotal: TextView


    // database
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order)
        inItId()
        btnOrderClickEvent()
        spinnerPayMethodEvent()
    }


    private fun spinnerPayMethodEvent() {

        ArrayAdapter.createFromResource(
            this,
            R.array.pay_method_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinnerPayMethod.adapter = adapter
        }
    }

// add order to firebase after click btnOrder
    @SuppressLint("SimpleDateFormat")
    private fun btnOrderClickEvent() {
        btnOrder.setOnClickListener {
            if (edtFullName.text.isEmpty()) {
                edtFullName.error = "Please enter your full name"
                return@setOnClickListener
            }
            if (edtPhoneNumber.text.isEmpty()) {
                edtPhoneNumber.error = "Please enter your phone number"
                return@setOnClickListener
            }
            if (edtAddress.text.isEmpty()) {
                edtAddress.error = "Please enter your address"
                return@setOnClickListener
            }
            val fullName = edtFullName.text.toString()
            val phoneNumber = edtPhoneNumber.text.toString()
            val address = edtAddress.text.toString()
            val message = edtMessage.text.toString()
            val payMethod = spinnerPayMethod.selectedItem.toString()
            val total = txtTotal.text.toString().replace("¥", "").trim().toInt()
            val userId = Utils.current_User.id
            val time = Calendar.getInstance().time
            Log.d("Time",time.toString())


            mDatabase = FirebaseDatabase.getInstance()
            mDatabaseReference = mDatabase.getReference("order")
            // create id order
            val newOrderRef = mDatabaseReference.push()
            val orderId = newOrderRef.key
            val orderModel = OrderModel(
                orderId.toString(),
                fullName,
                phoneNumber,
                address,
                message,
                payMethod,
                total,
                time,
                Utils.listCart
            )
            // push order to firebase
            orderId?.let {
                mDatabaseReference.child(userId)
                    .child(orderId)
                    .setValue(orderModel)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Order Successful", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener {
                        Log.e("Error", "{${it.message}}")
                    }
            }
            //delete cart of currentUserId after order on Firebase
            val mDatabaseReference2 = mDatabase.getReference("cart")
            userId.let {
                mDatabaseReference2.child(it)
                    .removeValue()
            }
            Utils.listCart.clear()
            progressBar.visibility = View.VISIBLE
            btnOrder.isEnabled = false
            Handler().postDelayed({
                progressBar.visibility = View.GONE
                startActivity(Intent(this@OrderActivity, MainActivity::class.java))
                finish()
            }, 2000)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun totalPrice() {
        var sum: Int = 0
        if (Utils.listCart.isNotEmpty()) {
            for (i in 0..<Utils.listCart.size) {
                sum += Utils.listCart[i].price * Utils.listCart[i].quantity
            }
        }
        txtTotal.text = "¥$sum"
    }


    private fun inItId() {
        txtTotal = findViewById(R.id.txtTotal)
        edtFullName = findViewById(R.id.edtFullName)
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber)
        edtAddress = findViewById(R.id.edtAddress)
        edtMessage = findViewById(R.id.edtMessage)
        btnOrder = findViewById(R.id.btnOrder)
        spinnerPayMethod = findViewById(R.id.spinnerPayMethod)
        progressBar = findViewById(R.id.progressBar)

    }

    override fun onResume() {
        super.onResume()
        totalPrice()
    }
}