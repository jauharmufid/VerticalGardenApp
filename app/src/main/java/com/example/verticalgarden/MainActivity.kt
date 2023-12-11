package com.example.verticalgarden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.text.InputType
import android.view.View
import com.example.verticalgarden.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        btnSignupListener()
        btnForgotpass()
        setupEyeButton()
    }
    private fun btnForgotpass(){
        binding.forgotpass.setOnClickListener {
            startActivity(Intent(this, ForgotpassActivity::class.java))
        }
    }
    private fun btnSignupListener(){
        binding.Signup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    private fun setupEyeButton() {
        val passwordEditText = binding.addpass
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
            }
        })
    }
}