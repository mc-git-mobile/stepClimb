package com.example.stepclimb

import android.hardware.SensorEventListener
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorEvent
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter
import java.io.*
import java.io.File


class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorMan : SensorManager
    private var accel : Sensor ?= null
    private var stepB = false
    private var climbB = false
    var infoS = arrayListOf<String>()
    var infoC = arrayListOf<String>()


    private var arrayAdapter: ArrayAdapter<String>? = null
    private var listView: ListView? = null
    private var listSensorData = arrayListOf<String>()  //used to store sensor data

    val fileS = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS), "step.txt")
    val fileC = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS), "climb.txt")




    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    override fun onSensorChanged(event: SensorEvent?) {

        var text1:TextView = findViewById(R.id.text1)


        text1.text = "x = ${event!!.values[0]}\n\n" +
                    "y = ${event.values[1]}\n\n" +
                    "z = ${event.values[2]}\n\n"

        infoS.add(" x = ${event!!.values[0]}, " +
                " y = ${event.values[1]}, " +
                " z = ${event.values[2]}  +")

        if (stepB == true) {
            infoS.add(" x = ${event!!.values[0]}, " +
                    " y = ${event.values[1]}, " +
                    " z = ${event.values[2]}  +")


        }
        if (climbB == true) {
            infoC.add(" x = ${event!!.values[0]}, " +
                    " y = ${event.values[1]}, " +
                    " z = ${event.values[2]}  +")

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        listView = findViewById(R.id.list)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView?.adapter = arrayAdapter



        var step: Button = findViewById(R.id.step)
        var climb: Button = findViewById(R.id.climb)

        sensorMan = getSystemService(Context.SENSOR_SERVICE) as SensorManager


        step.setOnClickListener {

            if (stepB == false) {
                stepB = true
                //val file = File(fileNameStep)
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
                //val file = File(fileNameClimb)
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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem)= when(item.itemId){
        R.id.view -> {
            var textD:TextView = findViewById(R.id.text1)
            textD.visibility = View.INVISIBLE
            var textM:TextView = findViewById(R.id.text3)
            textM.visibility = View.INVISIBLE

            var bC:Button = findViewById(R.id.climb)
            bC.visibility = View.INVISIBLE
            var bS:Button = findViewById(R.id.step)
            bS.visibility = View.INVISIBLE

            var data:ListView = findViewById(R.id.list)
            data.visibility = View.VISIBLE


            true
        }
        R.id.write -> {

            fileS.writeText("walk =  " + infoS.toString())
            infoS.clear()
            fileC.writeText("climb =  " + infoC.toString())
            infoC.clear()

            true
        }
        R.id.back -> {
            var textD:TextView = findViewById(R.id.text1)
            textD.visibility = View.VISIBLE
            var textM:TextView = findViewById(R.id.text3)
            textM.visibility = View.VISIBLE

            var bC:Button = findViewById(R.id.climb)
            bC.visibility = View.VISIBLE
            var bS:Button = findViewById(R.id.step)
            bS.visibility = View.VISIBLE

            var data:ListView = findViewById(R.id.list)
            data.visibility = View.INVISIBLE




            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }

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
