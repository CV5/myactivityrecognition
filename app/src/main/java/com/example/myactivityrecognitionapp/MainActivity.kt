package com.example.myactivityrecognitionapp
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private val transitionsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.mylistview)



        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, transitionsList)
        listView.adapter = adapter
        val transitions = mutableListOf<ActivityTransition>()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.STILL)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.STILL)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.RUNNING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.RUNNING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()

        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.IN_VEHICLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.IN_VEHICLE)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()

        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.ON_FOOT)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build()
        transitions += ActivityTransition.Builder()
            .setActivityType(DetectedActivity.ON_FOOT)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()

        val request = ActivityTransitionRequest(transitions)
        val intent = Intent(this, com.example.myactivityrecognitionapp.ActivityRecognition::class.java)

        val myPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            PendingIntent.getBroadcast(
                this,
                12,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                this,
                12,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }


        val task = ActivityRecognition.getClient(this)
            .requestActivityTransitionUpdates(request, myPendingIntent)

        task.addOnSuccessListener {
            transitionsList.add("Activity transitions successfully registered.")
            adapter.notifyDataSetChanged()
            Log.d("MActRecognition", "Activity transitions successfully registered.")
        }

        task.addOnFailureListener { e: Exception ->
            transitionsList.add("error ___>: ${e.localizedMessage}")

            Log.d("ActivityRecognition", "error ___>: ${e.localizedMessage}")
        }

        val filter = IntentFilter("com.example.myactivityrecognitionapp.UPDATE_UI")
//        registerReceiver(uiReceiver, filter, RECEIVER_NOT_EXPORTED)
        LocalBroadcastManager.getInstance(this).registerReceiver(uiReceiver, filter)

    }

    private val uiReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.v("uiReceiver","onReceive")
            val transitionType = intent?.getStringExtra("transition")
            Log.d("MActyRecognition", "Received transition: $transitionType")

            if (transitionType != null) {
                transitionsList.add(transitionType)
                adapter.notifyDataSetChanged()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(uiReceiver)
    }
}
