package com.example.orderfood.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.orderfood.R
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private  lateinit var btnRegister : Button
    private lateinit var progress_bar:ProgressBar
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        inItId()
        mAuth = FirebaseAuth.getInstance()
        btnRegister.setOnClickListener {
            registerNewUser()
        }

    }

    private fun registerNewUser() {
        // Take the value of two edit texts in Strings
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()

        // Validations for input email and password
        if (email.isEmpty() || password.isEmpty()|| confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please enter credentials", Toast.LENGTH_SHORT).show()
        }else if(password != confirmPassword) {
            Toast.makeText(this, "Please enter correct password", Toast.LENGTH_SHORT).show()
        }
        else {
            // create new user or register new user
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progress_bar.visibility = View.VISIBLE
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    // if the user created intent to login activity
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    // Registration failed
                    Toast.makeText(this, "Registration failed!! " + " Please try again later", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun inItId() {
        edtEmail = findViewById(R.id.edtEmail)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        edtPassword = findViewById(R.id.edtPassword)
        btnRegister = findViewById(R.id.btnRegister)
        progress_bar = findViewById(R.id.progress_bar)
    }
}