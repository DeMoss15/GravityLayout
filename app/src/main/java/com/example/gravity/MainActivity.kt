package com.example.gravity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), TiltListener {

    var textView: TextView? = null

    val sensor: TiltSensor by lazy {
        TiltSensorImpl(this)
            .apply { addListener(this@MainActivity) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById<TextView>(R.id.tvAngles)
        sensor.register()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensor.unregister()
    }

    override fun onTilt(azimuth: Double, pitch: Double, roll: Double) {
        textView?.text = "azimuth = ${azimuth.roundToInt()} rad ${(azimuth * 180 / Math.PI).roundToInt()} degree\n" +
            "pitch = ${pitch.roundToInt()} rad ${(pitch * 180 / Math.PI).roundToInt()}  degree\n" +
            "roll = ${roll.roundToInt()} rad ${(roll * 180 / Math.PI).roundToInt()} degree"
    }
}