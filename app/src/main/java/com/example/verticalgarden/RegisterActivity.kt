package com.example.verticalgarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.example.verticalgarden.databinding.ActivityMainBinding
import com.example.verticalgarden.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        btnSigninListener()
        setupEyeButton()
    }
    private fun btnSigninListener(){
        binding.signinonsignup.setOnClickListener {
            startActivity(Intent(this, ActivityMainBinding::class.java))
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
}