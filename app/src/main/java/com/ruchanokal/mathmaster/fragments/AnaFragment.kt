package com.ruchanokal.mathmaster.fragments

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.work.Operation
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.games.Games
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.ruchanokal.mathmaster.R
import com.ruchanokal.mathmaster.classes.AlarmReceiver
import kotlinx.android.synthetic.main.fragment_ana.*
import kotlinx.android.synthetic.main.fragment_seviye_bir.*
import kotlinx.coroutines.*
import java.util.*




class AnaFragment : Fragment() {

    private val RC_LEADERBOARD_UI = 9004

    lateinit var sharedPreferences: SharedPreferences
    lateinit var preferences : SharedPreferences
    lateinit var birinciPreferences: SharedPreferences
    lateinit var mAuth: FirebaseAuth
    lateinit var user : FirebaseUser
    private lateinit var db : FirebaseFirestore

    var numberOne : String = ""
    var numberTwo : String = ""
    var numberThree : String = ""
    var userUid = ""

    var levelOne : Int = 0
    var levelTwo : Int = 0
    var levelThree : Int = 0

    var reference: ListenerRegistration? = null

    private lateinit var alarmManager : AlarmManager

    var job : Job? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =  inflater.inflate(R.layout.fragment_ana, container, false)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){

            relativeLayoutForAnaFragmentBanner.visibility = View.VISIBLE

            val adRequest = AdRequest.Builder().build()
            adViewAna.loadAd(adRequest)


        }

        preferences = context?.getSharedPreferences("anafragmentBir",Context.MODE_PRIVATE)!!

        sharedPreferences = context?.getSharedPreferences("notification",Context.MODE_PRIVATE)!!


        val isChecked = sharedPreferences.getBoolean("isChecked",true)



        if (isChecked == true) {

            createNotificationChannel()

            setAlarm()

        }

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val intent = requireActivity().intent

        val definiteNumber = intent.getIntExtra("definite",0)


        job = CoroutineScope(Dispatchers.IO).launch {

            if ( definiteNumber == 1) {

                val kullaniciAdi = intent.getStringExtra("kullaniciAdi")
                userUid = mAuth.currentUser?.uid.toString()

                kullaniciAdiAnaFragment.setText(kullaniciAdi)


                val hashMap = hashMapOf<Any,Any>()
                mAuth.currentUser?.email?.let { hashMap.put("email", it) }
                if (kullaniciAdi != null) {
                    hashMap.put("kullaniciAdi",kullaniciAdi)
                }


                db.collection("Users").document(userUid).set(hashMap).addOnSuccessListener {


                }

            } else if ( definiteNumber == 2) {

                userUid = mAuth.currentUser?.uid.toString()

                val email = mAuth.currentUser?.email

                if ( mAuth.currentUser!!.displayName != null && !mAuth.currentUser!!.displayName.equals("")) {


                    kullaniciAdiAnaFragment.text = mAuth.currentUser!!.displayName


                }

                reference = db.collection("Users").whereEqualTo("email",email).addSnapshotListener { value, error ->

                    if (error != null){

                        Toast.makeText(context,"Bir hata oluştu",Toast.LENGTH_LONG).show()

                    } else {

                        if ( value != null) {
                            if ( !value.isEmpty ) {

                                val documents = value.documents

                                for ( document in documents) {


                                    kullaniciAdiAnaFragment.text = document.get("kullaniciAdi") as String

                                    reference?.remove()
                                }

                            }

                        }


                    }

                }

            } else if ( definiteNumber == 3) {

                if (mAuth.currentUser != null ) {

                    user = mAuth.currentUser!!

                    if (user != null) {

                        kullaniciAdiAnaFragment.text = user.displayName

                    }
                }
            }



           withContext(Dispatchers.Main){

               rateThisApp.setOnClickListener {
                   try {

                       val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + requireActivity().packageName))
                       startActivity(intent)

                   } catch ( e : ActivityNotFoundException){

                       val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + requireActivity().packageName))
                       startActivity(intent)

                   }
               }


               shareThisApp.setOnClickListener {

                   try {

                       val intent = Intent(Intent.ACTION_SEND)
                       intent.setType("text/plain")

                       val link = "https://play.google.com/store/apps/details?id=" + requireActivity().packageName
                       intent.putExtra(Intent.EXTRA_TEXT,link)
                       startActivity(Intent.createChooser(intent,"Bir Platform Seç"))

                   } catch ( e : Exception) {

                       Toast.makeText(activity, requireContext().getString(R.string.hata_olustu_toast),Toast.LENGTH_SHORT).show()

                   }

               }

               imageViewSettings.setOnClickListener {

                   val action = AnaFragmentDirections.actionAnaFragmentToSettingsFragment()
                   Navigation.findNavController(it).navigate(action)


               }


               arguments?.let {

                   val enYuksekSkorSeviyeBir = AnaFragmentArgs.fromBundle(it).enyuksekskor1
                   val enYuksekSkorSeviyeIki = AnaFragmentArgs.fromBundle(it).enyuksekskor2
                   val enYuksekSkorSeviyeUc = AnaFragmentArgs.fromBundle(it).enyuksekskor3

                   val levelSeviyeBir = AnaFragmentArgs.fromBundle(it).levelbirincioyun
                   val levelSeviyeIki = AnaFragmentArgs.fromBundle(it).levelikincioyun
                   val levelSeviyeUc = AnaFragmentArgs.fromBundle(it).levelucuncuoyun


                   if ( enYuksekSkorSeviyeBir.toInt() > 0) {

                       preferences.edit().putString("birinciEnYuksek",enYuksekSkorSeviyeBir).apply()

                   }

                   numberOne = preferences.getString("birinciEnYuksek", 0.toString()).toString()
                   enYuksekSkorTextAnaSayfa.text = "${requireContext().getString(R.string.en_yuksek_skor)}: " + numberOne


                   if (levelSeviyeBir > 1 ){

                       preferences.edit().putInt("birinciLevel",levelSeviyeBir).apply()

                   }

                   levelOne = preferences.getInt("birinciLevel",1)
                   //levelSeviyeBirAnaFragment.text = "Level ${levelOne}"
                   levelSeviyeBirAnaFragment.text = requireContext().getString(R.string.level) + " " + levelOne.toString()


                   if ( enYuksekSkorSeviyeIki.toInt() > 0) {

                       preferences.edit().putString("ikinciEnYuksek",enYuksekSkorSeviyeIki).apply()

                   }

                   numberTwo = preferences.getString("ikinciEnYuksek", 0.toString()).toString()
                   enYuksekSkorTextAnaSayfa2.text = "${requireContext().getString(R.string.en_yuksek_skor)}: " + numberTwo


                   if (levelSeviyeIki > 1 ){

                       preferences.edit().putInt("ikinciLevel",levelSeviyeIki).apply()

                   }

                   levelTwo = preferences.getInt("ikinciLevel",1)
                   levelSeviyeIkiAnaFragment.text = requireContext().getString(R.string.level) + " " + levelTwo.toString()


                   if ( enYuksekSkorSeviyeUc.toInt() > 0) {

                       preferences.edit().putString("ucuncuEnYuksek",enYuksekSkorSeviyeUc).apply()

                   }

                   numberThree = preferences.getString("ucuncuEnYuksek", 0.toString()).toString()
                   enYuksekSkorTextAnaSayfa3.text = "${requireContext().getString(R.string.en_yuksek_skor)}: " + numberThree


                   if (levelSeviyeUc > 1 ){

                       preferences.edit().putInt("ucuncuLevel",levelSeviyeUc).apply()

                   }

                   levelThree = preferences.getInt("ucuncuLevel",1)
                   levelSeviyeUcAnaFragment.text = requireContext().getString(R.string.level) + " " + levelThree.toString()


               }




               val highestTopScore = numberOne.toInt() + numberTwo.toInt() + numberThree.toInt()

               val account = GoogleSignIn.getLastSignedInAccount(context)

               if (account != null) {

                   Games.getLeaderboardsClient(context, account)
                       .submitScore(getString(R.string.leaderboard_id_all_games), highestTopScore.toLong())

                   Games.getLeaderboardsClient(context, account)
                       .submitScore(getString(R.string.leaderboard_id_1), numberOne.toLong())

                   Games.getLeaderboardsClient(context, account)
                       .submitScore(getString(R.string.leaderboard_id_2), numberTwo.toLong())

                   Games.getLeaderboardsClient(context, account)
                       .submitScore(getString(R.string.leaderboard_id_3), numberThree.toLong())

               }

               leaderBoard.setOnClickListener {

                   leaderBoardprogressBarAnaFragment.visibility = View.VISIBLE

                   if (account != null) {

                       Games.getLeaderboardsClient(context, account)
                           .getLeaderboardIntent(requireContext().getString(R.string.leaderboard_id_all_games))
                           .addOnSuccessListener { intent ->

                               startActivityForResult(intent, RC_LEADERBOARD_UI)

                               leaderBoardprogressBarAnaFragment.visibility = View.GONE

                           }.addOnFailureListener {

                               leaderBoardprogressBarAnaFragment.visibility = View.GONE

                           }

                   }

               }


               enYuksekSkorSeviyeBir.setOnClickListener {

                   leaderBoardprogressBarAnaFragment.visibility = View.VISIBLE

                   if (account != null) {

                       Games.getLeaderboardsClient(context, account)
                           .getLeaderboardIntent(requireContext().getString(R.string.leaderboard_id_1))
                           .addOnSuccessListener { intent ->

                               startActivityForResult(intent, RC_LEADERBOARD_UI)

                               leaderBoardprogressBarAnaFragment.visibility = View.GONE

                           }.addOnFailureListener {

                               leaderBoardprogressBarAnaFragment.visibility = View.GONE

                           }

                   }

               }


               enYuksekSkorSeviyeIki.setOnClickListener {

                   leaderBoardprogressBarAnaFragment.visibility = View.VISIBLE

                   if (account != null) {

                       Games.getLeaderboardsClient(context, account)
                           .getLeaderboardIntent(requireContext().getString(R.string.leaderboard_id_2))
                           .addOnSuccessListener { intent ->

                               startActivityForResult(intent, RC_LEADERBOARD_UI)

                               leaderBoardprogressBarAnaFragment.visibility = View.GONE

                           }.addOnFailureListener {

                               leaderBoardprogressBarAnaFragment.visibility = View.GONE

                           }

                   }

               }

               enYuksekSkorSeviyeUc.setOnClickListener {

                   leaderBoardprogressBarAnaFragment.visibility = View.VISIBLE

                   if (account != null) {

                       Games.getLeaderboardsClient(context, account)
                           .getLeaderboardIntent(requireContext().getString(R.string.leaderboard_id_3))
                           .addOnSuccessListener { intent ->

                               startActivityForResult(intent, RC_LEADERBOARD_UI)

                               leaderBoardprogressBarAnaFragment.visibility = View.GONE

                           }.addOnFailureListener {

                               leaderBoardprogressBarAnaFragment.visibility = View.GONE

                           }

                   }

               }






               birinciOyunBaslamaButonu.setOnClickListener {

                   val kullaniciAdim = kullaniciAdiAnaFragment.text.toString()

                   val action = AnaFragmentDirections.actionAnaFragmentToSeviyeBirFragment(kullaniciAdim)
                   Navigation.findNavController(it).navigate(action)

               }

               ikinciOyunBaslamaButonu.setOnClickListener {

                   val action = AnaFragmentDirections.actionAnaFragmentToSeviyeIkiFragment()
                   Navigation.findNavController(it).navigate(action)

               }

               ucuncuOyunBaslamaButonu.setOnClickListener {

                   val action = AnaFragmentDirections.actionAnaFragmentToSeviyeUcFragment()
                   Navigation.findNavController(it).navigate(action)

               }

               nasilOynanirImage1.setOnClickListener {

                   val action = AnaFragmentDirections.actionAnaFragmentToNasilOynanirFragment(keyInteger = 1)
                   Navigation.findNavController(it).navigate(action)

               }

               nasilOynanirImage2.setOnClickListener {

                   val action = AnaFragmentDirections.actionAnaFragmentToNasilOynanirFragment(keyInteger = 2)
                   Navigation.findNavController(it).navigate(action)

               }

               nasilOynanirImage3.setOnClickListener {

                   val action = AnaFragmentDirections.actionAnaFragmentToNasilOynanirFragment(keyInteger = 3)
                   Navigation.findNavController(it).navigate(action)


               }



           }




        }


        

        val callback = object  : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)


    }



    private fun setAlarm() {

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 16
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0

        if (calendar.time.compareTo(Date()) < 0) calendar.add(Calendar.DAY_OF_MONTH, 1)

        val intent = Intent(context?.applicationContext, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context?.applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context?.applicationContext?.getSystemService(ALARM_SERVICE) as AlarmManager?


        if (alarmManager != null) {

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)

        }

    }

    private fun createNotificationChannel() {

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){

            val channelName = requireContext().getString(R.string.hatirlatici)
            val description = requireContext().getString(R.string.hatirlatici_aciklama)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel("notificationChannel",channelName,importance)
            channel.description = description

            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)

        }

    }

    override fun onDestroy() {

        birinciPreferences = context?.getSharedPreferences("birinci",Context.MODE_PRIVATE)!!
        birinciPreferences.edit().remove("reklamPeriyodu").apply()

        super.onDestroy()
    }


    override fun onDestroyView() {
        super.onDestroyView()

        reference?.remove()

    }










}