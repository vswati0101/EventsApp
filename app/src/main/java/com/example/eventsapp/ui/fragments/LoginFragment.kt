package com.example.eventsapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eventsapp.R
import com.example.eventsapp.ui.EventsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupText: TextView
    private lateinit var userName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        auth = FirebaseAuth.getInstance()
        emailEditText = view.findViewById(R.id.emailEt)
        passwordEditText = view.findViewById(R.id.passET)
        loginButton = view.findViewById(R.id.button)
        signupText = view.findViewById(R.id.signup_text)
        userName = view.findViewById(R.id.usernameEt)
        loginButton.setOnClickListener {
            login()
        }
        signupText.setOnClickListener {
            navigateToSignUpFragment()
        }

        return view
    }

    private fun login() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val username = userName.text.toString()

        if (username.isEmpty()) {
            userName.error = "Please enter your username"
            return
        }

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val sharedPreferences =
                        requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("userName", email)
                    editor.apply()
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    Log.d("LoginFragment", "navigate")
                    val intent = Intent(requireActivity(), EventsActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "User not found"
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email or password"
                        else -> "Login failed"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            val intent = Intent(requireActivity(), EventsActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun navigateToSignUpFragment() {
        val signupFragment = SignUpFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, signupFragment)
        transaction.commit()
    }
}
