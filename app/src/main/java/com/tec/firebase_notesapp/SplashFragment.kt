package com.tec.firebase_notesapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var nav:NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        var isLogin:Boolean=auth.currentUser!=null
        val hadler=Handler(Looper.myLooper()!!)
       hadler.postDelayed({
           if(isLogin)
                 nav.navigate(R.id.action_splashFragment3_to_homeFragment2)
           else
               nav.navigate(R.id.action_splashFragment3_to_siginInFragment2)
       },2000)
    }

    private fun init(view: View) {
        auth = FirebaseAuth.getInstance()
        nav= Navigation.findNavController(view)
    }
}
