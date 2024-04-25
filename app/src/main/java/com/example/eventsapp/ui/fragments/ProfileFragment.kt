package com.example.eventsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.eventsapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var db: FirebaseFirestore
    private lateinit var profileImageView: ImageView
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var updateProfileButton: Button
    private lateinit var logoutTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser!!
        db = FirebaseFirestore.getInstance()

        profileImageView = view.findViewById(R.id.profile_image_view)
        emailEditText = view.findViewById(R.id.emailprofileEt)
        passwordEditText = view.findViewById(R.id.passprofileET)
        updateProfileButton = view.findViewById(R.id.buttonprofile)
        logoutTextView = view.findViewById(R.id.login_text)

        emailEditText.setText(currentUser.email)

        updateProfileButton.setOnClickListener {
            val newEmail = emailEditText.text.toString()
            val newPassword = passwordEditText.text.toString()

            // Update email
            if (newEmail != currentUser.email) {
                updateEmail(newEmail)
            }

            // Update password
            if (newPassword.isNotEmpty()) {
                updatePassword(newPassword)
            }
        }

        logoutTextView.setOnClickListener {
            mAuth.signOut()
            Toast.makeText(context, "Logged Out!", Toast.LENGTH_SHORT).show()
            navigateToLoginFragment()
        }

        return view
    }

    private fun updateEmail(newEmail: String) {
        currentUser.updateEmail(newEmail).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                }
            }
    }

    private fun updatePassword(newPassword: String) {
        currentUser.updatePassword(newPassword).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Password Updated!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToLoginFragment() {
        hideBottomNavigation()
        val loginFragment = LoginFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.eventsNavHostFragment, loginFragment)
        transaction.commit()
    }

    private fun hideBottomNavigation() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
    }
}