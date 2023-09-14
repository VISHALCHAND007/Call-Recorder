package com.example.callrecorder.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.callrecorder.MainActivity
import com.example.callrecorder.utils.Constants.Companion.TAG

class PhoneCallReceiver: BroadcastReceiver() {
    private lateinit var callRecorder: CallRecorder
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        val incomingCallerNum = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        callRecorder = CallRecorder(context!!, incomingCallerNum.toString())
        if(state == "IDLE") {
            callRecorder.stopRecorder()
        }
        if(state == "OFFHOOK") {
            callRecorder.startMediaRecorder()
        }
    }
}