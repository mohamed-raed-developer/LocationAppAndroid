package com.example.locationappandroid

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class MyService : Service() {


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()


//        mp = MediaPlayer.create(this, R.raw.mm)
//        mp.isLooping = true
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            startMyOwnForeground()
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            startForeground(
                1,
                Notification()
            )
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
//        mp.start()
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                Activity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
//            return
//        }
//        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        //return super.onStartCommand(intent, flags, startId)
        //return START_NOT_STICKY //Service not restarted
        //return START_STICKY //Service Restarted with Null Intent Data
        return START_STICKY //Service Restarted with Intent Data
    }

    override fun onDestroy() {
        super.onDestroy()
        //mp.pause()
        //mp.stop()
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show()
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
//                        txtLat.text = location.latitude.toString()
//                        txtLng.text = location.longitude.toString()
                    }
                    // Few more things we can do here:
                    // For example: Update the location of user on server
                }
            },
            Looper.myLooper()
        )
    }
}