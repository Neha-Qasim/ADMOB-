package com.neha.admobdemo.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.neha.admobdemo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Navigate to Register screen
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Login button click
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmail.error = "Email required"
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Invalid email"
                return@setOnClickListener
            }
            if (pass.length < 8) {
                binding.etPassword.error = "Password must be 8+ characters"
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    startActivity(Intent(this, Screen1Activity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.localizedMessage ?: "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
