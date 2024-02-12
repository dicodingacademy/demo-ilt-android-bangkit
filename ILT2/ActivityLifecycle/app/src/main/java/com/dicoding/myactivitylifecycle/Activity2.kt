package com.dicoding.myactivitylifecycle

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Activity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        Log.d(TAG, "Activity 2 - onCreate")
    }

    fun launchActivity3(view: View) {
        startActivity(Intent(this, Activity3::class.java))
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Activity 2 - onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Activity 2 - onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Activity 2 - onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Activity 2 - onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "Activity 2 - onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Activity 2 - onDestroy")
    }

    fun requestCameraPermission(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                101 // Request code for camera permission
            )
        } else {
            // Camera permission already granted
            Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
            // Proceed with camera functionality
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
                // Proceed with camera functionality
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "Activity2"
    }

}