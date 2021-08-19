package com.ruchanokal.mathmaster.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.ruchanokal.mathmaster.R
import com.ruchanokal.mathmaster.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_ana.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private var sifre : String = ""
    private var sifre2 : String = ""
    var userUid = ""
    lateinit var user : FirebaseUser
    private lateinit var db : FirebaseFirestore
    private lateinit var hashMap : HashMap<Any,Any>
    private lateinit var kullaniciAdi : String
    private lateinit var email : String
    private lateinit var listener : ListenerRegistration



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        kayitOlButton.setOnClickListener {

            progressBarSignUp.visibility = View.VISIBLE

            email = editTextEmailKayit.text.toString()
            kullaniciAdi = editTextKullaniciAdiKayit.text.toString()
            sifre = editTextSifreKayit.text.toString()
            sifre2 = editTextSifreKayit2.text.toString()


            hashMap = hashMapOf<Any,Any>()
            hashMap.put("email",email)
            hashMap.put("kullaniciAdi",kullaniciAdi)


            databaseCollection()


        }

    }

    private fun databaseCollection() {


        val query = db.collection("Users").whereEqualTo("kullaniciAdi",kullaniciAdi)

        listener  = query.addSnapshotListener { value, error ->

            if (value !=null) {
                println("null değil")

                if ( !value.isEmpty) {
                    println("empty değil")

                    Toast.makeText(context,requireActivity().getString(R.string.lutfen_baska_bir_kullanici_adi_deneyin),Toast.LENGTH_LONG).show()

                    progressBarSignUp.visibility = View.GONE

                } else {

                    println("empty")

                    kontroller()

                }

            } else {

                println("null")

                kontroller()

            }


        }

    }


    private fun kontroller() {


        if (email.equals("") || kullaniciAdi.equals("") || sifre.equals("")){

            listener.remove()

            Toast.makeText(activity,requireActivity().getString(R.string.lutfen_gerekli_alanlari_doldurunuz),Toast.LENGTH_LONG).show()

            progressBarSignUp.visibility = View.GONE

        } else if (!sifre.equals(sifre2)){

            listener.remove()

            Toast.makeText(activity,requireActivity().getString(R.string.sifreler_ayni_olmalidir),Toast.LENGTH_LONG).show()

            progressBarSignUp.visibility = View.GONE

        }  else  {

            mAuth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener { task ->

                if (task.isSuccessful){

                    listener.remove()

                    userUid = mAuth.currentUser?.uid.toString()

                    progressBarSignUp.visibility = View.GONE

                    val intent = Intent(activity,MainActivity::class.java)
                    intent.putExtra("kullaniciAdi",kullaniciAdi)
                    intent.putExtra("definite",1)
                    startActivity(intent)
                    requireActivity().finish()

                    Toast.makeText(activity,"${requireActivity().getString(R.string.hosgeldin)} ${kullaniciAdi}",Toast.LENGTH_LONG).show()

                }

            }.addOnFailureListener { exception ->


                try {
                    throw exception
                }   catch (e: FirebaseAuthUserCollisionException) {

                    listener.remove()

                    Toast.makeText(activity,requireActivity().getString(R.string.eposta_kullaniliyor),Toast.LENGTH_LONG).show()
                    progressBarSignUp.visibility = View.GONE

                } catch(e : FirebaseAuthWeakPasswordException) {

                    listener.remove()

                    Toast.makeText(activity,requireActivity().getString(R.string.alti_haneli_sifre),Toast.LENGTH_LONG).show()
                    progressBarSignUp.visibility = View.GONE

                } catch (e: FirebaseNetworkException) {

                    listener.remove()

                    Toast.makeText(activity,requireActivity().getString(R.string.lutfen_internet_baglantinizi_kontrol_edin),Toast.LENGTH_LONG).show()
                    progressBarSignUp.visibility = View.GONE

                } catch(e : FirebaseAuthInvalidCredentialsException) {

                    listener.remove()

                    Toast.makeText(activity,e.localizedMessage,Toast.LENGTH_LONG).show()
                    progressBarSignUp.visibility = View.GONE

                } catch (e: Exception) {

                    listener.remove()

                    Toast.makeText(activity,e.localizedMessage,Toast.LENGTH_LONG).show()
                    progressBarSignUp.visibility = View.GONE
                }

            }


        }


    }




}