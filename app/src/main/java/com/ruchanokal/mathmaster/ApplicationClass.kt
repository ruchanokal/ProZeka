package com.ruchanokal.mathmaster

import android.app.Application
import com.onesignal.OneSignal

class ApplicationClass : Application() {

    private val ONESIGNAL_APP_ID = "cb7fcd38-f2b8-46eb-bc36-4332d2b05c00"




    override fun onCreate() {
        super.onCreate()

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)

    }



}