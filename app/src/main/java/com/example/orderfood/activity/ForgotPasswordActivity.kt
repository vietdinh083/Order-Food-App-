package com.example.orderfood.activity

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.orderfood.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtConfirmEmail: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var btnResetPassword: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        mAuth = FirebaseAuth.getInstance()
        inItId()
        btnResetPassword.setOnClickListener {
            resetPassWord()
        }
    }

    private fun resetPassWord() {
        val email = edtEmail.text.toString().trim()
        val confirmEmail = edtConfirmEmail.text.toString().trim()
        if (email.isEmpty() || confirmEmail.isEmpty()) {
            edtEmail.error = "Please enter email"
            edtConfirmEmail.error = "Please enter confirm email"
        }
        if (email != confirmEmail) {
            edtEmail.error = "Please enter correct email"
            edtConfirmEmail.error = "Please enter correct confirm email"
        } else {
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressBar.visibility = View.VISIBLE
                        Toast.makeText(this, "Password reset email sent", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private fun inItId() {
        edtEmail = findViewById(R.id.edtEmail)
        edtConfirmEmail = findViewById(R.id.edtConfirmEmail)
        progressBar = findViewById(R.id.progress_bar)
        btnResetPassword = findViewById(R.id.btnResetPassword)

    }
}