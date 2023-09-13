package com.example.callrecorder.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.example.callrecorder.MainActivity
import com.example.callrecorder.MainActivity.Companion.TAG

class PhoneCallReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)

        if(state == TelephonyManager.EXTRA_STATE_RINGING) {
            Log.e(TAG, "Incoming call")
            val incomingCallerNum = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            Log.e(TAG, "Incoming call $incomingCallerNum")

            if(incomingCallerNum != null) {
                Log.e(TAG, incomingCallerNum.toString())
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra(TAG, incomingCallerNum)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intent)
            }
        }
    }
}