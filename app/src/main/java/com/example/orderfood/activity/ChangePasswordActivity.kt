package com.example.orderfood.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.orderfood.R
import com.example.orderfood.Utils.Utils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Suppress("DEPRECATION")
class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var edtCurrentPassword: EditText
    private lateinit var edtNewPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnChange: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        initId()
        btnChangeClickEvent()
    }

    private fun btnChangeClickEvent() {
        btnChange.setOnClickListener {
            changePassword()
            btnChange.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                btnChange.isEnabled = true
                progressBar.visibility = View.GONE  // Ẩn ProgressBar sau 3 giây
            },3000)
        }
    }

    private fun changePassword() {
        val user = Firebase.auth.currentUser
        val currentPassword = edtCurrentPassword.text.toString().trim()
        val newPassword = edtNewPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()
        if (currentPassword.isEmpty() || currentPassword != Utils.current_User.password) {
            edtCurrentPassword.error = "Please enter your current password or enter true password"
            return
        }

        if (newPassword.isEmpty()) {
            edtNewPassword.error = "Please enter your new password"
            return
        }
        if (confirmPassword.isEmpty()) {
            edtConfirmPassword.error = "Please enter your confirm password"
        }
        if (newPassword != confirmPassword) {
            edtConfirmPassword.error = "Password does not match"
            return
        } else {
            user!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        btnChange.isEnabled = false
                        progressBar.visibility = View.VISIBLE

                        Toast.makeText(this,"Change password successfully",Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this,"Change password failed",Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }

    @SuppressLint("RestrictedApi")
    private fun initId() {
        edtCurrentPassword = findViewById(R.id.edtCurrentPassword)
        edtNewPassword = findViewById(R.id.edtNewPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnChange = findViewById(R.id.btnChange)
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
                finish()
        }

    }
}