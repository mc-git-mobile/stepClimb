package com.example.stepclimb

import android.hardware.SensorEventListener
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorEvent
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorMan : SensorManager
    private var accel : Sensor ?= null
    private var stepB = false
    private var climbB = false
    val defName = "src/main/resources/def.text"
    val fileNameClimb = "src/main/resources/climb.text"
    val fileNameStep = "src/main/resources/step.txt"
    val file = File(defName)
    //val climbFile = File(fileNameClimb)
    //val stepFile = File(fileNameStep)




    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    override fun onSensorChanged(event: SensorEvent?) {
        text1.text= "x = ${event!!.values[0]}\n\n" +
                    "y = ${event.values[1]}\n\n" +
                    "z = ${event.values[2]}"

        //file.writeText("x = ${event!!.values[0]}\n\n" +
       //                    "y = ${event.values[1]}\n\n" +
        //                    "z = ${event.values[2]}\n\n")


        file.writeText("cool")

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var step: Button =findViewById(R.id.step)
        var climb: Button = findViewById(R.id.climb)

        sensorMan = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //turn on sensor when walking button is pressed
        step.setOnClickListener {
            if (stepB == false) {
                stepB = true
                val file = File(fileNameStep)
                sensorMan.registerListener(this, sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

            }
            else if (stepB == true) {
                stepB = false
                sensorMan.unregisterListener(this)
            }

        }
        //turn onb sensor when climbing button is pressed
        climb.setOnClickListener {
            if (climbB == false) {
                climbB = true
                val file = File(fileNameClimb)
                sensorMan.registerListener(this, sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

            }
            else if (climbB == true) {
                climbB = false
                sensorMan.unregisterListener(this)
            }

        }
        //On create





        //on create
    }


    override fun onPause() {
        super.onPause()
        stepB = false
        climbB = false
        sensorMan?.unregisterListener(this)
    } // end on pause

    override fun onResume() {
        super.onResume()
        sensorMan?.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
    } // end on resume
} //end class
