package com.example.myactivityrecognitionapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity

class ActivityRecognition : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("MActyRecognition", "onReceive" + intent!!.action)
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)!!
            for (event in result.transitionEvents) {
                val transitionType = when (event.activityType) {
                    DetectedActivity.IN_VEHICLE -> "Estás en un vehículo"
                    DetectedActivity.ON_BICYCLE -> "Estás en una bicicleta"
                    DetectedActivity.ON_FOOT -> "Estás caminando"
                    DetectedActivity.STILL -> "Estás quieto"
                    DetectedActivity.UNKNOWN -> "Actividad desconocida"
                    DetectedActivity.TILTING -> "Estás inclinándote"
                    DetectedActivity.WALKING -> "Estás caminando"
                    DetectedActivity.RUNNING -> "Estás corriendo"
                    else -> "Actividad desconocida"
                }

                Toast.makeText(context, "Transition: $transitionType", Toast.LENGTH_LONG).show()

                val uiIntent = Intent("com.example.myactivityrecognitionapp.UPDATE_UI")
                uiIntent.putExtra("transition", transitionType)

                LocalBroadcastManager.getInstance(context!!).sendBroadcast(uiIntent)

                Log.d("MActyRecognition", "Transition: $transitionType")
            }
        }else{
            Log.d("MActyRecognition", "Intent null")
        }
    }
}
