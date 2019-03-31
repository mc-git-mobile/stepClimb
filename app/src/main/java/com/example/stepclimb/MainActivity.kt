package com.example.stepclimb

import android.hardware.SensorEventListener
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorEvent
import android.os.Environment
import android.util.Log
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
import java.lang.Math.sqrt


class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorMan : SensorManager
    private var accel : Sensor ?= null
    private var stepB = false
    private var climbB = false
    var infoX = arrayListOf<Float>()
    var infoZ = arrayListOf<Double>()

    var i = 0
    var j = 3
    var h = 2
    var step = 0

    var stepCounter = 0
    var climbCounter = 0



    var infoS = arrayListOf<String>()
    var infoC = arrayListOf<String>()
    var infoD = arrayListOf<String>()
    var infoN = arrayListOf<Double>()


    var d:Double = 0.0
    var c:Double = 0.0





    private var arrayAdapter: ArrayAdapter<String>? = null
    private var listView: ListView? = null
    private var listSensorData = arrayListOf<String>()  //used to store sensor data

    val fileS = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS), "step.txt")
    val fileC = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS), "climb.txt")
    val fileD = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS), "D.txt")
    val fileN = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS), "N.txt")




    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    override fun onSensorChanged(event: SensorEvent?) {

        var text1:TextView = findViewById(R.id.text1)
        //var text2:TextView = findViewById(R.id.text1)

        var stepBox:TextView = findViewById(R.id.stepCount)
        var climbBox:TextView = findViewById(R.id.climbCount)


        infoX.add(event!!.values[1])

        c = (event.values[0].toDouble()*event.values[0].toDouble())+(event.values[1].toDouble()*event.values[1].toDouble())+(event.values[2].toDouble()*event.values[2].toDouble())

        d = sqrt(c)

        infoZ.add(d)

        //infoD.add(d.toString())

        if (infoZ.size > 3) {

            if ( (infoZ[i-1] > infoZ[i-2]) && (infoZ[i-1] > infoZ[i]) ) {
                infoN.add(infoZ[i-1])
                h++
                j++

            }
            if ( (infoZ[i-1] < infoZ[i-2]) && (infoZ[i-1] < infoZ[i]) ) {
                infoN.add(infoZ[i-1])
                h++
                j++
            }

            Log.i("infoN ", infoN.toString())


        }
        //Log.i("infoN", infoN[i].toString())
        Log.i("infoN size", infoN.size.toString())
        Log.i("j", j.toString())


        if (infoN.size > j){
            if ((infoN[j] - infoN[h]) > 0.05 /*|| (infoN[h] - infoN[j]) < (- 0.05)*/ ){

                step ++
                Log.i("info n h", infoN[h].toString())
                Log.i("info n j", infoN[j].toString())
                Log.i("info n h- j", (infoN[h]- infoN[j]).toString())
                //climbBox.setText((infoN[h]- infoN[j]).toString())


                //h++
                //j++
                //Log.i("info n h", infoN[h].toString())
                //Log.i("info n j", infoN[j].toString())
                //stepBox.setText((infoN[h]- infoN[j]).toString())


            }
        }

        stepBox.setText(step.toString())
        //Log.i("info n h", infoN[h].toString())
        //Log.i("info n j", infoN[j].toString())


        Log.i("step count", step.toString())



        if (infoD.size > 3) {

            if ( (infoD[i-1] > infoD[i-2]) && (infoD[i-1] > infoD[i]) ) {
                if (stepB == true) {
                    stepCounter +=1
                }

                if (climbB == true) {
                    climbCounter += 1
                }
            }
        }



        if (infoX.size > 3) {

            if ( (infoX[i-1] > infoX[i-2]) && (infoX[i-1] > infoX[i]) ) {
                //stepCounter +=1
                //climbCounter += 1

                // stepBox.setText(stepCounter)
            }
        }

        if (stepB == true) {
            //stepBox.setText(stepCounter.toString())
        }

        if (climbB == true) {
            //climbBox.setText(climbCounter.toString())
        }


        //stepBox.setText(stepCounter.toString())

        i +=1

        text1.text = "x = ${event!!.values[0]}\n\n" +
                    "y = ${event.values[1]}\n\n" +
                    "z = ${event.values[2]}\n\n"



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
                if(climbB == true) {
                    climbB = false
                }
                //val file = File(fileNameStep)
                sensorMan.registerListener(this, sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
                //sensorMan.sensor


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
                if(stepB == true) {
                    stepB = false
                }

                //val file = File(fileNameClimb)
                sensorMan.registerListener(this, sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_STATUS_ACCURACY_HIGH)

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

            var textC:TextView = findViewById(R.id.climbCount)
            textC.visibility = View.INVISIBLE

            var textS:TextView = findViewById(R.id.stepCount)
            textS.visibility = View.INVISIBLE

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

            fileD.writeText("D =  " + infoD.toString())
            infoD.clear()

            fileN.writeText("N =  " + infoN.toString())
            infoN.clear()

            true
        }

        R.id.back -> {
            var textD:TextView = findViewById(R.id.text1)
            textD.visibility = View.VISIBLE
            var textC:TextView = findViewById(R.id.climbCount)
            textC.visibility = View.VISIBLE

            var textS:TextView = findViewById(R.id.stepCount)
            textS.visibility = View.VISIBLE

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
