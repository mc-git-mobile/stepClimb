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
    private var stepClicked = false
    private var climbClicked = false
    var infoX = arrayListOf<Float>()

    var sensorEventCounter = 0
    var filterEventCounter = -1


    // just using these to compare one value to the next but my idea didnt work anyway
    var counterPost = 3 //counter to get next value
    var counterPre = 2 // counter to get previous value

    var step = 0 // steps taken

    var stepCounter = 0
    var climbCounter = 0



    var infoStep = arrayListOf<String>()
    var infoClimb = arrayListOf<String>()
    //var infoPythagUnfilteredExtra = arrayListOf<String>()
    var infoPythagFiltered = arrayListOf<Double>()
    var infoPythagUnfiltered = arrayListOf<Double>()

    var pU:Double = 0.0


    var pythagUnfiltered:Double = 0.0



    private var arrayAdapter: ArrayAdapter<String>? = null // adapter for list view if needed
    private var listView: ListView? = null // list view for action bar to show data if needed

    private var listSensorData = arrayListOf<String>()  //used to store sensor data will implement eventually


    // file to save step data
    var fileStep = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS), "step.txt")
    // file to save climb data
    var fileClimb = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS), "climb.txt")
    // file to save unfiltered pythag data
    var filePythagUnfiltered = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS), "pythagoreanUnfiltered.txt")
    // file to save filtered pythag data
    var filePythag = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOCUMENTS), "pythagoreanFiltered.txt")


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {

        var text1:TextView = findViewById(R.id.text1)
        //var text2:TextView = findViewById(R.id.text1)

        var stepView:TextView = findViewById(R.id.stepCount)
        var climbView:TextView = findViewById(R.id.climbCount)


        infoX.add(event!!.values[1]) // adding just y data to and array on each sensor event to test a counting method

        // combine axis using pythagorean theorem
        pU = (event.values[0].toDouble()*event.values[0].toDouble())+(event.values[1].toDouble()*event.values[1].toDouble())+(event.values[2].toDouble()*event.values[2].toDouble())
        pythagUnfiltered = sqrt(pU)  //getting combination of all axis

        // adding pythag data to unfiltered array
        infoPythagUnfiltered.add(pythagUnfiltered)




        // filtering pythagorean data to show only peaks and valleys
        if (infoPythagUnfiltered.size > 3) {

            if ( (infoPythagUnfiltered[sensorEventCounter-1] > infoPythagUnfiltered[sensorEventCounter-2]) && (infoPythagUnfiltered[sensorEventCounter-1] > infoPythagUnfiltered[sensorEventCounter]) ) {
                infoPythagFiltered.add(infoPythagUnfiltered[sensorEventCounter-1])
                filterEventCounter +=1



                //  ||                                                         ||
                //  \/  counters not being used probably useless all together  \/
                //counterPre++
                //counterPost++
            }
            if ( (infoPythagUnfiltered[sensorEventCounter-1] < infoPythagUnfiltered[sensorEventCounter-2]) && (infoPythagUnfiltered[sensorEventCounter-1] < infoPythagUnfiltered[sensorEventCounter]) ) {
                infoPythagFiltered.add(infoPythagUnfiltered[sensorEventCounter-1])
                filterEventCounter +=1


                //  ||                                                         ||
                //  \/  counters not being used probably useless all together  \/
                //counterPre++
                //counterPost++
            }
        }

        /*
        if (infoPythag.size > j){
            if ((infoPythag[j] - infoPythag[h]) > 0.05 /*|| (infoN[h] - infoN[j]) < (- 0.05)*/ ){
                step ++
            }
        }
        stepView.setText(step.toString()) // set step view to step counter
        */



        // just testing a method to see if i could only count peaks but peaks don't represent steps
        if (infoPythagFiltered.size > 3) {

            if ( (infoPythagFiltered[filterEventCounter-1] > infoPythagFiltered[filterEventCounter-2]) && (infoPythagFiltered[filterEventCounter-1] > infoPythagFiltered[filterEventCounter]) && (infoPythagFiltered[filterEventCounter-1] > 10.5) ) {
                if (stepClicked == true) {
                    stepCounter +=1

                    Log.i ("info step counter", stepCounter.toString())
                    Log.i ("info step pF -1", infoPythagFiltered[filterEventCounter-1].toString())
                    Log.i ("info step pF -2", infoPythagFiltered[filterEventCounter-2].toString())
                    Log.i ("info step pF", infoPythagFiltered[filterEventCounter-0].toString())

                }

                if (climbClicked == true) {
                    climbCounter += 1
                    Log.i ("info climb counter", climbCounter.toString())

                }
            }
        }


        /*
        if (infoX.size > 3) {
            if ( (infoX[i-1] > infoX[i-2]) && (infoX[i-1] > infoX[i]) ) {
                //stepCounter +=1
                //climbCounter += 1
                // stepBox.setText(stepCounter)
            }
        }*/


        if (stepClicked == true) {
            stepView.setText(stepCounter.toString())
        }

        if (climbClicked == true) {
            climbView.setText(climbCounter.toString())
        }


        sensorEventCounter +=1 // increment counter every time sensor event happens
        //filterEventCounter +=1



        // displaying current axis data in text main text field
        text1.text = "x = ${event!!.values[0]}\n\n" +
                    "y = ${event.values[1]}\n\n" +
                    "z = ${event.values[2]}\n\n"


        // array to store steps adding event values to is if step button is pressed
        if (stepClicked == true) {
            infoStep.add(" x = ${event!!.values[0]}, " +
                         " y = ${event.values[1]}, " +
                         " z = ${event.values[2]}  +")
        }
        // array to store stair steps adding event values to is if climb button is pressed
        if (climbClicked == true) {
            infoClimb.add(" x = ${event!!.values[0]}, " +
                          " y = ${event.values[1]}, " +
                          " z = ${event.values[2]}  +")
        } //  Not being used right now

        /*
        if (stepB == true) {
            infoStep.add(" x = ${event!!.values[0]}, " +
                    " y = ${event.values[1]}, " +
                    " z = ${event.values[2]}  +")
        }

        if (climbB == true) {
            infoClimb.add(" x = ${event!!.values[0]}, " +
                            " y = ${event.values[1]}, " +
                        " z = ${event.values[2]}  +")
        }*/

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //list view that is opened from the action bar
        listView = findViewById(R.id.list)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView?.adapter = arrayAdapter



        var step: Button = findViewById(R.id.step)
        var climb: Button = findViewById(R.id.climb)

        sensorMan = getSystemService(Context.SENSOR_SERVICE) as SensorManager  // sensor manager

        // on click listener that sets boolean value C
        step.setOnClickListener {

            if (stepClicked == false) {
                stepClicked = true
                if(climbClicked == true) {
                    climbClicked = false
                }
                //val file = File(fileNameStep)
                sensorMan.registerListener(this, sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
                //sensorMan.sensor


            }
            else if (stepClicked == true) {
                stepClicked = false
                sensorMan.unregisterListener(this)
            }

        }
        //turn onb sensor when climbing button is pressed
        climb.setOnClickListener {
            if (climbClicked == false) {
                climbClicked = true
                if(stepClicked == true) {
                    stepClicked = false
                }

                //val file = File(fileNameClimb)
                sensorMan.registerListener(this, sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST)

            }
            else if (climbClicked == true) {
                climbClicked = false
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
            //view button is to just view the list of sensor data but it is not implemented yet

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
            //used to save all the arrays to files if the save button is pressed

            fileStep.writeText("walk =  " + infoStep.toString())
            infoStep.clear()

            fileClimb.writeText("climb =  " + infoClimb.toString())
            infoClimb.clear()

            filePythag.writeText("Pythagorean Filtered Data (X,Y,Z) =  " + infoPythagFiltered.toString())
            infoPythagFiltered.clear()

            filePythagUnfiltered.writeText("Pythagorean Unfiltered Data (X, Y, Z) =  " + infoPythagUnfiltered.toString())
            infoPythagUnfiltered.clear()

            true
        }

        R.id.clear -> {
            //used to save all the arrays to files if the save button is pressed

            infoStep.clear()
            infoClimb.clear()
            infoPythagFiltered.clear()
            infoPythagUnfiltered.clear()

            true
        }

        R.id.back -> {
            // sets list to invisible and goes back to original state
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

    // stops sensor and changes booleans when ap paused
    override fun onPause() {
        super.onPause()
        stepClicked = false
        climbClicked = false
        sensorMan?.unregisterListener(this)
    } // end on pause

    // turns sensor back on on resume but removed because i want the sensor to start only when button is pressed
    override fun onResume() {
        super.onResume()
        //sensorMan?.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
    } // end on resume
} //end class
