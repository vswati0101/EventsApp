package com.example.eventsapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var loginText: TextView
    private lateinit var signUpButton: Button
    private lateinit var userName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        auth = FirebaseAuth.getInstance()
        emailEditText = view.findViewById(R.id.emailEt)
        passwordEditText = view.findViewById(R.id.passET)
        confirmPasswordEditText = view.findViewById(R.id.confirmPassEt)
        signUpButton = view.findViewById(R.id.button)
        loginText = view.findViewById(R.id.login_text)
        userName = view.findViewById(R.id.usernameEt)
        signUpButton.setOnClickListener {
            signUp()
        }
        loginText.setOnClickListener {
            navigateToLoginFragment()
        }
        return view
    }

    private fun signUp() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()
        val username = userName.text.toString()

        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }
        if (username.isEmpty()) {
            userName.error = "Please enter your username"
            return
        }
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val sharedPreferences =
                        requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("userName", username)
                    editor.apply()
                    Toast.makeText(context, "Sign up successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), EventsActivity::class.java)
                    requireActivity().startActivity(intent)
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthUserCollisionException -> "Email already exists"
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email or password format"
                        else -> "Sign up failed"
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToLoginFragment() {
        val loginFragment = LoginFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, loginFragment)
        transaction.commit()
    }
}
