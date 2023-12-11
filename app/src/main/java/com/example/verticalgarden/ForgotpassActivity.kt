package com.example.verticalgarden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.example.verticalgarden.databinding.ActivityForgotpassBinding

class ForgotpassActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotpassBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityForgotpassBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        setupEyeButton()
    }
    private fun setupEyeButton() {
        val passwordEditText = binding.resetconfirmpass
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