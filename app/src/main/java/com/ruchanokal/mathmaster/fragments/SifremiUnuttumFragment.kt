package com.ruchanokal.mathmaster.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ruchanokal.mathmaster.R
import kotlinx.android.synthetic.main.fragment_sifremi_unuttum.*


class SifremiUnuttumFragment : Fragment() {


    lateinit var mAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sifremi_unuttum, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()


        sifremiUnuttumTamamButton.setOnClickListener {


            if (sifremiUnuttumEditText.text.toString() != null && !sifremiUnuttumEditText.text.isEmpty()) {

                val email = sifremiUnuttumEditText.text.toString()

                mAuth.sendPasswordResetEmail(email).addOnFailureListener {

                    Toast.makeText(context,requireActivity().getString(R.string.eposta_gonderilemedi),Toast.LENGTH_LONG).show()

                }.addOnSuccessListener {

                    Toast.makeText(context,requireActivity().getString(R.string.eposta_gonderildi),Toast.LENGTH_LONG).show()


                }

            } else {

                Toast.makeText(context,requireActivity().getString(R.string.lutfen_kayitli_emailinizi_giriniz),Toast.LENGTH_LONG).show()

            }






        }


    }
}