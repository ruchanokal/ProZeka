package com.ruchanokal.mathmaster.fragments

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ruchanokal.mathmaster.R
import com.ruchanokal.mathmaster.activities.SignInActivity
import com.ruchanokal.mathmaster.classes.AlarmReceiver
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*
import java.util.concurrent.TimeUnit


class SettingsFragment : Fragment() {


    lateinit var mAuth: FirebaseAuth
    lateinit var user : FirebaseUser
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = context?.getSharedPreferences("notification",Context.MODE_PRIVATE)!!
        mAuth = FirebaseAuth.getInstance()

        val isChecked = sharedPreferences.getBoolean("isChecked",true)

        notificationSwitchCompat.isChecked = isChecked

        notificationSwitchCompat.setOnCheckedChangeListener { compoundButton, b ->


            if (!notificationSwitchCompat.isChecked) {


                val intent = Intent(activity,AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(activity,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
                val alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager

                alarmManager.cancel(pendingIntent)

                sharedPreferences.edit().putBoolean("isChecked",false).apply()


            } else {

                createNotificationChannel()

                val calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = 16
                calendar[Calendar.MINUTE] = 0
                calendar[Calendar.SECOND] = 0

                if (calendar.time.compareTo(Date()) < 0)
                    calendar.add(Calendar.DAY_OF_MONTH, 1)


                val intent = Intent(context?.applicationContext,AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context?.applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
                val alarmManager = context?.applicationContext?.getSystemService(ALARM_SERVICE) as AlarmManager


                if (alarmManager != null) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                }

                sharedPreferences.edit().putBoolean("isChecked",true).apply()

            }


        }

        exitAppRelativeLayout.setOnClickListener {

            user = mAuth.currentUser!!

            if (user != null) {

                mAuth.signOut()

                val intent = Intent(activity, SignInActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }


        }

        val callback = object  : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)




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
}