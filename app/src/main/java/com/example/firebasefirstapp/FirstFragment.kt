package com.example.firebasefirstapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.firebasefirstapp.databinding.FragmentFirstBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            // Check if user is signed in (non-null) and update UI accordingly.
            val currentUser = auth.currentUser
            if(currentUser != null){
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        binding.messageView.text = "Current user ${currentUser?.email}"
        binding.signIn.setOnClickListener {
            val email = binding.emailInputField.text.toString().trim()
            val password = binding.passwordInputField.text.toString().trim()
            if (email.isEmpty()) {
                binding.emailInputField.error = "No email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.passwordInputField.error = "No password"
                return@setOnClickListener
            }
            // https://firebase.google.com/docs/auth/android/password-auth
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                } else {
                    binding.messageView.text = task.exception?.message
                }
            }
        }
        binding.buttonCreateUser.setOnClickListener {
            val email = binding.emailInputField.text.toString().trim()
            val password = binding.passwordInputField.text.toString().trim()
            if (email.isEmpty()) {
                binding.emailInputField.error = "No email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.passwordInputField.error = "No password"
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.messageView.text = "User created. Now please login"
                    // Alternative: goto next fragment (no need to login after register)
                } else {
                    binding.messageView.text = task.exception?.message
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}