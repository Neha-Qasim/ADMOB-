package com.neha.admobdemo.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.neha.admobdemo.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
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
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                binding.progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    // Save user info in Realtime DB
                    val uid = auth.currentUser?.uid ?: ""
                    val userMap = mapOf("email" to email)
                    dbRef.child(uid).setValue(userMap)

                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Screen1Activity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.localizedMessage ?: "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
