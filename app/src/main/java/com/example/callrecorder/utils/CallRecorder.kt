package com.example.callrecorder.utils

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.callrecorder.MainActivity
import com.example.callrecorder.utils.Constants.Companion.TAG
import java.io.File
import java.io.IOException
import java.util.Date

class CallRecorder(
    private val context: Context,
    phoneNum: String
) {
    private val recorder = MediaRecorder()
    private var date = Date()
    private val fileName: String = "${phoneNum}_${date}".replace(" ", "_")+".mp3"
    private val outputFile = File(context.filesDir, fileName)

    @RequiresApi(Build.VERSION_CODES.O)
    fun startMediaRecorder(): Boolean {
        try {
            if(!fileName.contains("null")) {
                recorder.reset()
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                recorder.setAudioSamplingRate(8000)
                recorder.setAudioEncodingBitRate(12200)
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                recorder.setOutputFile(outputFile.absolutePath)

                val errorListener =
                    MediaRecorder.OnErrorListener { arg0, arg1, arg2 ->
                        Log.e(TAG, "OnErrorListener $arg1,$arg2")
                    }
                recorder.setOnErrorListener(errorListener)

                val onInfoListener = MediaRecorder.OnInfoListener { arg0, arg1, arg2 ->
                    Log.e(TAG, "OnErrorListener $arg1,$arg2")
                }

                recorder.setOnInfoListener(onInfoListener)
                recorder.prepare()
                // Sometimes prepare takes some time to complete
                Thread.sleep(2000);
                recorder.start()
            }
            return true
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d(TAG, it) }
            return false;
        }
    }

    fun stopRecorder() {
        try {
            Thread.sleep(2000)
            recorder.stop()
            recorder.release()
            val recordedFilePath = outputFile.absolutePath
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(recordedFilePath)
            mediaPlayer.prepare()
            mediaPlayer.start()

            MainActivity().getListOfFiles()
        } catch (e: Exception) {
            Log.e(TAG, "MediaRecorder is not in a valid state for stopping.")
            Log.e(TAG, e.message.toString())
        }
    }


    private fun playRecordedAudio() {
        val recordedFilePath = outputFile.absolutePath
        if (recordedFilePath.isNotBlank()) {
            val mediaPlayer = MediaPlayer()

            try {
                mediaPlayer.setDataSource(recordedFilePath)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: IOException) {
                Log.e(TAG, "Error playing recorded audio file: ${e.message}")
            }
        } else {
            Log.e(TAG, "Recorded audio file path is empty or invalid.")
        }
    }
}

