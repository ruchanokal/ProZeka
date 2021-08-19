package com.ruchanokal.mathmaster.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import com.ruchanokal.mathmaster.R
import com.ruchanokal.mathmaster.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_sign_in.*
import java.util.*


class SignInFragment : Fragment() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private lateinit var googleSignInClient : GoogleSignInClient
    private val RC_SIGN_IN = 123

    private lateinit var callbackManager: CallbackManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        callbackManager = CallbackManager.Factory.create()

        facebookGirisYap.setOnClickListener {

        progressBarSignIn.visibility = View.VISIBLE

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "public_profile"))
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                println("onSuccess")
                handleFacebookAccessToken(loginResult.accessToken)


            }

            override fun onCancel() {
                println("onCancel")
                progressBarSignIn.visibility = View.GONE

            }

            override fun onError(error: FacebookException) {
                println("onError")
                progressBarSignIn.visibility = View.GONE

            }
        })


        }

        /*
        buttonFacebookLogin.setFragment(this)
        buttonFacebookLogin.setPermissions("email", "public_profile")

        buttonFacebookLogin.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                println("onSuccess")
                handleFacebookAccessToken(loginResult.accessToken)
            }
            override fun onCancel() {
                println("onCancel")

            }
            override fun onError(error: FacebookException) {
                println("onError")
            }
        })
        */

        mAuth = FirebaseAuth.getInstance()

        ///////////// IF CURRENT USER IS NOT NULL /////////////////

        if (mAuth.currentUser != null ) {

            user = mAuth.currentUser!!

            if (user != null) {

                val intent = Intent(activity,MainActivity::class.java)
                intent.putExtra("definite",2)
                startActivity(intent)
                requireActivity().finish()

            }

        }

        ////////////// GOOGLE SIGN OPTIONS //////////////////////////

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }!!

        googleGirisYap.setOnClickListener { signInGoogle() }


        ///////////////// EMAIL PASSWORD SIGN IN //////////////////////

        girisYapButton.setOnClickListener { signIn() }


        ///////////////// EMAIL PASSWORD SIGN UP //////////////////////

        kayitOlText.setOnClickListener {

            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            Navigation.findNavController(it).navigate(action)

        }


        ///////////////// FORGOT PASSWORD - ŞİFREMİ UNUTTUM //////////////////////

        sifremiUnuttumText.setOnClickListener {

            val action = SignInFragmentDirections.actionSignInFragmentToSifremiUnuttumFragment()
            Navigation.findNavController(it).navigate(action)

        }


    }



    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        activity?.let {
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        println("Task is successful")
                        val user = mAuth.currentUser
                        val name = user?.displayName
                        val photo = user?.photoUrl

                        val intent = Intent(context,MainActivity::class.java)
                        intent.putExtra("PhotoUrl",photo)
                        intent.putExtra("name",name)
                        intent.putExtra("definite",3)
                        startActivity(intent)
                        progressBarSignIn.visibility = View.GONE

                        requireActivity().finish()

                    } else {
                        // If sign in fails, display a message to the user.

                        println("hata1: "+ task.exception)
                        progressBarSignIn.visibility = View.GONE

                        Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }.addOnFailureListener { e ->

                    progressBarSignIn.visibility = View.GONE
                    println("hata: " + e.localizedMessage)
                }
        }
    }


    ////////////////////// ****GOOGLE SIGN IN***** /////////////////////////////
    ///////////////////////////////////////////////////////////////////////////


    private fun signInGoogle() {

        progressBarSignIn.visibility = View.VISIBLE

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {

                println("büyük hata: " + e.localizedMessage)
                // Google Sign In failed, update UI appropriately
                    Toast.makeText(context,requireActivity().getString(R.string.hata_olustu_toast),Toast.LENGTH_LONG).show()

                    progressBarSignIn.visibility = View.GONE

            }
        }

    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        activity?.let {
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val googleUser = mAuth.currentUser
                        val name = googleUser?.displayName
                        val photo = googleUser?.photoUrl

                        val intent = Intent(context,MainActivity::class.java)
                        intent.putExtra("photoUrl",photo)
                        intent.putExtra("name",name)
                        intent.putExtra("definite",3)
                        startActivity(intent)
                        requireActivity().finish()
                        progressBarSignIn.visibility = View.GONE

                    } else {

                        progressBarSignIn.visibility = View.GONE
                        // If sign in fails, display a message to the user.
                        Toast.makeText(context,requireActivity().getString(R.string.hata_olustu_toast),Toast.LENGTH_LONG).show()

                    }
                }.addOnFailureListener {

                    println("büyük hata2: " + it.localizedMessage)

                }
        }
    }

    ////////////////////// **** EMAIL AND PASSWORD SIGN IN ***** /////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////

    private fun signIn() {

        progressBarSignIn.visibility = View.VISIBLE

        val email  = editTextEmail.text.toString()
        val password = editTextSifre.text.toString()


        if ( email.equals("") && password.equals("")) {

            Toast.makeText(activity,requireActivity().getString(R.string.lutfen_gerekli_alanlari_doldurunuz),Toast.LENGTH_LONG).show()

            progressBarSignIn.visibility = View.GONE

        } else if ( password.equals("")) {

            Toast.makeText(activity,requireActivity().getString(R.string.lutfen_sifrenizi_giriniz),Toast.LENGTH_LONG).show()

            progressBarSignIn.visibility = View.GONE

        } else if(email.equals("") ) {

            Toast.makeText(activity,requireActivity().getString(R.string.lutfen_kayitli_emailinizi_giriniz),Toast.LENGTH_LONG).show()

            progressBarSignIn.visibility = View.GONE

        } else {

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {

                if (it.isSuccessful){

                    val intent = Intent(activity,MainActivity::class.java)
                    intent.putExtra("definite",2)
                    startActivity(intent)
                    requireActivity().finish()
                    progressBarSignIn.visibility = View.GONE


                } else {

                    try {
                        throw it.getException()!!
                    }   catch (e: FirebaseAuthUserCollisionException) {

                        println("kullanıcılar eşleşmiyor")

                        Toast.makeText(activity,e.localizedMessage,Toast.LENGTH_LONG).show()
                        progressBarSignIn.visibility = View.GONE

                    } catch (e: FirebaseAuthEmailException) {

                        println("email hatası")

                        Toast.makeText(activity,e.localizedMessage,Toast.LENGTH_LONG).show()
                        progressBarSignIn.visibility = View.GONE

                    } catch (e: FirebaseAuthInvalidUserException) {

                        println(e)

                        Toast.makeText(activity,requireActivity().getString(R.string.boyle_bir_kullanici_yok),Toast.LENGTH_LONG).show()
                        progressBarSignIn.visibility = View.GONE

                    } catch (e: FirebaseNetworkException) {

                        println(e)

                        Toast.makeText(activity,requireActivity().getString(R.string.lutfen_internet_baglantinizi_kontrol_edin),Toast.LENGTH_LONG).show()
                        progressBarSignIn.visibility = View.GONE

                    } catch (e: FirebaseAuthInvalidCredentialsException) {


                        Toast.makeText(activity,e.localizedMessage,Toast.LENGTH_LONG).show()
                        progressBarSignIn.visibility = View.GONE

                    } catch (e: Exception) {

                        Toast.makeText(activity,e.localizedMessage,Toast.LENGTH_LONG).show()
                        progressBarSignIn.visibility = View.GONE

                    }

                }

            }

        }



    }


}