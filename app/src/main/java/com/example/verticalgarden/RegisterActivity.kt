package com.example.verticalgarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import com.example.verticalgarden.databinding.ActivityMainBinding
import com.example.verticalgarden.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        btnSigninListener()
        setupEyeButton()
        setupSignupButton()
    }

    private fun btnSigninListener(){
        binding.signinonsignup.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun setupEyeButton() {
        val passwordEditText = binding.signupcreatepass
        val passwordEditText2 = binding.signupconfirmpass
        val eyeButton = binding.eyeButton

        eyeButton.setOnClickListener(object : View.OnClickListener {
            var isPasswordVisible = false

            override fun onClick(view: View) {
                isPasswordVisible = !isPasswordVisible
                val imageResource = if (isPasswordVisible) R.drawable.eye_open else R.drawable.eye_close
                eyeButton.setImageResource(imageResource)
                val inputType = if (isPasswordVisible) {
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                } else {
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                passwordEditText.inputType = inputType
                passwordEditText.setSelection(passwordEditText.text.length)
                passwordEditText2.inputType = inputType
                passwordEditText2.setSelection(passwordEditText2.text.length)
            }
        })
    }

    private fun setupSignupButton() {
        binding.signupnbutton.setOnClickListener {
            val fullName = binding.signupfullname.text.toString()
            val email = binding.signupemail.text.toString()
            val password = binding.signupcreatepass.text.toString()
            val confirmPassword = binding.signupconfirmpass.text.toString()

            if (password == confirmPassword) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            startActivity(Intent(this, MainActivity::class.java))
                            Toast.makeText(baseContext, "Sign Up Success",
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(baseContext, "Passwords do not match.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}
