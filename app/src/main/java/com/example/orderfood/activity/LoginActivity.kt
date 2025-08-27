package com.example.orderfood.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.orderfood.R
import com.example.orderfood.Utils.Utils
import com.example.orderfood.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import io.paperdb.Paper

class LoginActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var checkBox: CheckBox
    private lateinit var txtForgetPassword: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        inItId()
        buttonAndTextEvents()


    }

    private fun buttonAndTextEvents() {
        btnLogin.setOnClickListener {
            loginUserAccount()
            btnLogin.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                btnLogin.isEnabled = true
                progressBar.visibility = View.GONE
            }, 3000)

        }
        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        txtForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

    }

    private fun loginUserAccount() {
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()

        // Validations for input email and password
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter credentials", Toast.LENGTH_LONG).show()
        } else {
            // login existing user
            if (checkBox.isChecked) {
                Paper.book().write("email", email)
                Paper.book().write("password", password)
                Paper.book().write("remember", true)
            } else {
                Paper.book().delete("email")
                Paper.book().delete("password")
                Paper.book().write("remember", false)
            }
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // login successful
                    //Log.d("TAG", "loginUserAccount: ${mAuth.currentUser?.uid}")
                    Utils.current_User =
                        UserModel(mAuth.currentUser?.uid.toString(), email, password)
                    progressBar.visibility = View.VISIBLE

                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this, "Login successful!!", Toast.LENGTH_LONG).show()

                } else {
                    // login failed
                    Log.e("Error", task.exception.toString())
                    Toast.makeText(this, "Login failed!!", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun inItId() {
        Paper.init(this);
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        checkBox = findViewById(R.id.checkbox)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.progress_bar)
        txtForgetPassword = findViewById(R.id.txtForgetPassword)
        if (Paper.book().read<Boolean>("remember", false) == true) {
            edtEmail.setText(Paper.book().read("email", ""))
            edtPassword.setText(Paper.book().read("password", ""))
            checkBox.isChecked = true
        }

    }

    override fun onResume() {
        super.onResume()
        if (Utils.current_User.email.isNotEmpty() && Utils.current_User.password.isNotEmpty()) {
            edtEmail.setText(Utils.current_User.email)
            edtPassword.setText(Utils.current_User.password)
        }
    }
}