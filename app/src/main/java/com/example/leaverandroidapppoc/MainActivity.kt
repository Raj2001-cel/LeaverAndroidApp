package com.example.leaverandroidapppoc

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.IOException

class MainActivity : AppCompatActivity() {
     lateinit var etEmail: EditText
     lateinit var  etPassword: EditText
     lateinit var etSignUpBtn:Button
    val MIN_PASSWORD_LENGTH = 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etEmail = findViewById(R.id.username)
        etPassword = findViewById(R.id.password)
        etSignUpBtn = findViewById(R.id.signupbtn);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        etSignUpBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                // Do some work here
                val email = etEmail.getText().toString().trim { it <= ' ' }
                val password = etPassword.getText().toString().trim { it <= ' ' }
                Log.d("credentials ",email+" "+password)

                    val context = applicationContext
                    val createUser = CreateUser()
                    try {
                        createUser.createUser(context,email, password)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                Toast.makeText(this@MainActivity, "Its toast!", Toast.LENGTH_SHORT).show()

            }

        })
    }

    fun validateInput(): Boolean {
        if (etEmail.text.toString() == "") {
            etEmail.error = "Please Enter Email"
            return false
        }
        if (etPassword.text.toString() == "") {
            etPassword.error = "Please Enter Password"
            return false
        }

        // checking the proper email format
        if (!isEmailValid(etEmail.text.toString())) {
            etEmail.error = "Please Enter Valid Email"
            return false
        }

        // checking minimum password Length
        if (etPassword.text.length < MIN_PASSWORD_LENGTH) {
            etPassword.error = "Password Length must be more than " + MIN_PASSWORD_LENGTH + "characters"
            return false
        }
        return true
    }

    fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}