package com.dicoding.myfirebasechat

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.myfirebasechat.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    // TODO [6] Instance Firebase Auth on LoginActivity.

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.login) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth

        // Initialize signInButton onClickListener

    }

    private fun signIn() {
        // TODO [7] Configuration for credentialManager
    }

    // TODO [8] Handle the successfully returned credential.

    private fun firebaseAuthWithGoogle(idToken: String) {
        // TODO [9] Sign in with Google using id Token from Credential
    }

    // TODO [10] Check if user is signed in (non-null) and update UI accordingly.
    override fun onStart() {
        super.onStart()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}