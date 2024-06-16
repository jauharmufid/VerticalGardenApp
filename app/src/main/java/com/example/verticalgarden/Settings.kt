package com.example.verticalgarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class Settings : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()

        val buttondashboard = findViewById<Button>(R.id.buttondashboard)
        buttondashboard.setOnClickListener {
            // Mulai DashboardTabLayout ketika buttondashboard diklik
            val intent = Intent(this, DashboardTabLayout::class.java)
            startActivity(intent)
        }

        val buttonresetpass = findViewById<Button>(R.id.buttonresetpass)
        buttonresetpass.setOnClickListener {
            // Mulai DashboardTabLayout ketika buttondashboard diklik
            val intent = Intent(this, ForgotpassActivity::class.java)
            startActivity(intent)
        }

        val buttonlogout = findViewById<Button>(R.id.buttonlogout)
        buttonlogout.setOnClickListener {
            // Keluar dari FirebaseAuth
            auth.signOut()

            // Keluar dari GoogleSignIn
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut()

            // Kembali ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null && acct is GoogleSignInAccount) {
            val personPhotoUrl = acct.photoUrl
            val personName = acct.displayName

            // Gunakan Glide atau library lainnya untuk memuat gambar dari URL ke ImageView.
            val backgroundProfile2 = findViewById<ImageView>(R.id.backgroundprofile2)
            Glide.with(this)
                .load(personPhotoUrl)
                .circleCrop()
                .into(backgroundProfile2)

            // Tampilkan nama pengguna di TextView dengan id username
            val usernameTextView = findViewById<TextView>(R.id.username)
            usernameTextView.text = personName
        } else {
            // Pengguna masuk dengan akun manual, tampilkan email mereka
            val userEmail = auth.currentUser?.email
            val usernameTextView = findViewById<TextView>(R.id.username)
            usernameTextView.text = userEmail
        }
    }
}
