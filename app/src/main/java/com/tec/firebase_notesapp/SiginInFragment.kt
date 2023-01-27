package com.tec.firebase_notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.tec.firebase_notesapp.databinding.FragmentSiginInBinding
import com.tec.firebase_notesapp.databinding.FragmentSingUpfragmentBinding

class SiginInFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSiginInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSiginInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        binding.textViewSignUp.setOnClickListener {
            navControl.navigate(R.id.action_siginInFragment_to_singUpfragment)
        }
        binding.nextBtn.setOnClickListener {
            val email=binding.emailEt.text.toString()
            val password=binding.passEt.text.toString()
            binding.progressBar.visibility=View.VISIBLE
            if(email.isNotEmpty() && password.isNotEmpty())
            {

                 login(email,password)

            }
            else
            {
                Toast.makeText(context,"ENter the details properly",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, password: String) {
                       auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                           if(it.isSuccessful)
                           {
                               navControl.navigate(R.id.action_siginInFragment_to_homeFragment)
                           }
                           else
                           {
                               Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()

                           }
                       }
        binding.progressBar.visibility=View.GONE
    }

    private fun init(view: View) {
       auth= FirebaseAuth.getInstance()
        navControl=Navigation.findNavController(view)
    }

}