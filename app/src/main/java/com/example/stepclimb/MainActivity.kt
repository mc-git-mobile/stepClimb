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
import kotlinx.android.synthetic.main.content_main.*
import java.io.*
import java.io.File
import java.lang.Math.abs
import java.lang.Math.sqrt


class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorMan : SensorManager
    private var accel : Sensor ?= null
    private var stepClicked = false
    private var climbClicked = false

    var sensorEventCounter = 0
    var filterEventCounter = -1

    var totVectStep = arrayListOf<Double>()
    var totVectClimb = arrayListOf<Double>()

    var totAveStep = arrayListOf<Double>()
    var totAveClimb = arrayListOf<Double>()


    var xVal = arrayListOf<Double>()
    var yVal = arrayListOf<Double>()
    var zVal = arrayListOf<Double>()

    var aveVis = false
    var minMaxVis = false
    var listStepVis = false
    var listClimbVis = false



    var xAve:Double = 0.0
    var yAve:Double = 0.0
    var zAve:Double = 0.0

    var threshHoldStep = 10.3
    var threshHoldClimb = 11.3


    var flag = 0

    var indexS = 0
    var indexC = 0
    //var index = 0




    var stepCounter = 0
    var climbCounter = 0

    var menuRawStep = arrayListOf<String>()
    var menuRawClimb = arrayListOf<String>()


    var menuAvg = arrayListOf<String>()

    var menuMaxMin = arrayListOf<String>()


    var infoStep = arrayListOf<String>()
    var infoClimb = arrayListOf<String>()
    //var infoPythagUnfilteredExtra = arrayListOf<String>()

    var infoPythagFiltered = arrayListOf<Double>()
    var infoPythagUnfiltered = arrayListOf<Double>()

    var vector = 0.0

    var climbAvg = 0.0
    var stepAvg = 0.0

    var stepMin = 0.0
    var climbMin = 0.0

    var stepMax = 0.0
    var climbMax = 0.0



    var pythagUnfiltered:Double = 0.0



    private var arrayAdapter: ArrayAdapter<String>? = null // adapter for list view if needed
    private var arrayAdapterClimb: ArrayAdapter<String>? = null // adapter for list view if needed

    private var arrayAdapterAve: ArrayAdapter<String>? = null // adapter for list view if needed
    private var arrayAdapterMinMax: ArrayAdapter<String>? = null // adapter for list view if needed

    private var listView: ListView? = null // list view for action bar to show data if needed
    private var listViewClimb: ListView? = null // list view for action bar to show data if needed

    private var listViewAvg: ListView? = null // list view for action bar to show data if needed
    private var listViewMinMax: ListView? = null // list view for action bar to show data if needed




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

        var dataView:TextView = findViewById(R.id.text1)

        var stepView:TextView = findViewById(R.id.stepCount)
        var climbView:TextView = findViewById(R.id.climbCount)


        //infoX.add(event!!.values[1]) // adding just y data to and array on each sensor event to test a counting method

        // combine axis using pythagorean theorem
        //pU = (event.values[0].toDouble()*event.values[0].toDouble())+(event.values[1].toDouble()*event.values[1].toDouble())+(event.values[2].toDouble()*event.values[2].toDouble())
        //pythagUnfiltered = sqrt(pU)  //getting combination of all axis

        // adding pythag data to unfiltered array
        //infoPythagUnfiltered.add(pythagUnfiltered)


        dataView.text = "x = ${event!!.values[0]}\n\n" +
                        "y = ${event.values[1]}\n\n" +
                        "z = ${event.values[2]}\n\n"


        xVal.add(event!!.values[0].toDouble())
        yVal.add(event.values[1].toDouble())
        zVal.add(event.values[2].toDouble())

        vector = (sqrt(((xVal[index]-xAve)* (xVal[index]-xAve))+ ((yVal[index] - yAve)*(yVal[index] - yAve)) + ((zVal[index] - zAve)*(zVal[index] - zAve))))
        //Log.i("info vector", vector.toString())

        //totVect.add(sqrt(((xVal[index]-xAve)* (xVal[index]-xAve))+ ((yVal[index] - yAve)*(yVal[index] - yAve)) + ((zVal[index] - zAve)*(zVal[index] - zAve))))
        if (stepClicked == true)
            totVectStep.add(vector)

            menuRawStep.add(totVectStep[index].toString())
            arrayAdapter?.notifyDataSetChanged()

        if (climbClicked == true)
            totVectClimb.add(vector)

            menuRawClimb.add(totVectStep[index].toString())
            arrayAdapterClimb?.notifyDataSetChanged()




        //menuRawStep.add(totVectStep[index].toString())
        //arrayAdapter?.notifyDataSetChanged()

        //menuAvg.add(index.toString())
        //arrayAdapterAve?.notifyDataSetChanged()

        menuMaxMin.add(index.toString())
        arrayAdapterMinMax?.notifyDataSetChanged()



        if (index>1 && stepClicked == true) {
            totAveStep.add(((totVectStep[index] + totVectStep[index - 1]) / 2))
            stepAvg = totAveStep[totAveStep.size-1]
        }

        if (index>1 && climbClicked == true) {
            totAveClimb.add(((totVectClimb[index] + totVectClimb[index - 1]) / 2))
            stepAvg = totAveStep[totAveStep.size-1]
        }

        /*
        if (index>1 && climbClicked == true) {
            totAve.add(((totVect[index] + totVect[index - 1]) / 2))
            if (stepClicked == true) {
                stepAvg = totAve[totAve.size-1]
            }
            if (climbClicked == true) {
                climbAvg = totAve[totAve.size-1]
            }
        }*/



        if (index > 1) {

            if(totAveStep[index-2] > threshHoldStep && flag == 0) {
                stepCounter ++
                flag = 1

            }
            else if (totAveStep[index-2] > threshHoldStep && flag == 1) { }

            if (totAveStep[index-2] < threshHoldStep && flag == 1) {
                flag = 0
            }
        }

        if (index > 1) {

            if(totAveClimb[index-2] > threshHoldClimb && flag == 0) {
                stepCounter ++
                flag = 1

            }
            else if (totAveClimb[index-2] > threshHoldClimb && flag == 1) { }

            if (totAveClimb[index-2] < threshHoldClimb && flag == 1) {
                flag = 0
            }
        }

        stepView.setText(stepCounter.toString())


        index ++

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        listView = findViewById(R.id.listStep)
        listView = findViewById(R.id.listClimb)

        listViewAvg = findViewById(R.id.ave)
        listViewMinMax = findViewById(R.id.minMax)


        arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuRawStep)
        arrayAdapterClimb = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuRawClimb)

        arrayAdapterAve = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuAvg)
        arrayAdapterMinMax = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuMaxMin)


        listView?.adapter = arrayAdapter
        listViewAvg?.adapter = arrayAdapterAve
        listViewMinMax?.adapter = arrayAdapterMinMax





        var step: Button = findViewById(R.id.step)
        var climb: Button = findViewById(R.id.climb)

        sensorMan = getSystemService(Context.SENSOR_SERVICE) as SensorManager  // sensor manager

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
                sensorMan.registerListener(this, sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

            }
            else if (climbClicked == true) {
                climbClicked = false
                sensorMan.unregisterListener(this)
            }

        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem)= when(item.itemId){

        R.id.maxMin -> {

            var dataS:ListView = findViewById(R.id.listStep)
            var dataC:ListView = findViewById(R.id.listClimb)

            var dataAve:ListView = findViewById(R.id.ave)

            if (listStepVis == true || aveVis == true || listClimbVis == true) {
                listStepVis = false
                listClimbVis = false
                aveVis = false

                dataS.visibility = View.INVISIBLE
                dataC.visibility = View.INVISIBLE
                dataAve.visibility = View.INVISIBLE
            }

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

            var dataMinMax:ListView = findViewById(R.id.minMax)
            dataMinMax.visibility = View.VISIBLE


            minMaxVis = true

            true
        }

        R.id.rawStep -> {
            var dataAve:ListView = findViewById(R.id.ave)
            var dataMinMax:ListView = findViewById(R.id.minMax)
            var dataRawClimb:ListView = findViewById(R.id.listClimb)



            if (aveVis == true || minMaxVis == true || listClimbVis == true) {
                aveVis = false
                minMaxVis = false
                listClimbVis = false

                dataAve.visibility = View.INVISIBLE
                dataMinMax.visibility = View.INVISIBLE
                dataRawClimb.visibility =View.INVISIBLE
            }

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

            var data:ListView = findViewById(R.id.listStep)
            data.visibility = View.VISIBLE

            listStepVis = true

            true
        }

        R.id.rawClimb -> {
            var dataAve:ListView = findViewById(R.id.ave)
            var dataMinMax:ListView = findViewById(R.id.minMax)
            var dataRawStep:ListView = findViewById(R.id.listStep)



            if (aveVis == true || minMaxVis == true || listStepVis == true) {
                aveVis = false
                minMaxVis = false
                listStepVis = false

                dataAve.visibility = View.INVISIBLE
                dataMinMax.visibility = View.INVISIBLE
                dataRawStep.visibility =View.INVISIBLE
            }

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

            var data:ListView = findViewById(R.id.listClimb)
            data.visibility = View.VISIBLE

            listClimbVis = true

            true
        }

        R.id.averages -> {

            menuAvg.add("Current Step Average =  " + stepAvg.toString())
            menuAvg.add("Current Climb Average =  " + climbAvg.toString())

            arrayAdapterAve?.notifyDataSetChanged()

            var dataS:ListView = findViewById(R.id.listStep)
            var dataC:ListView = findViewById(R.id.listClimb)
            var dataMinMax:ListView = findViewById(R.id.minMax)


            if (listStepVis == true || listClimbVis == true || minMaxVis == true) {

                listStepVis = false
                listClimbVis = false
                minMaxVis = false

                dataS.visibility = View.INVISIBLE
                dataC.visibility = View.INVISIBLE
                dataMinMax.visibility = View.INVISIBLE
            }

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

            var dataAve:ListView = findViewById(R.id.ave)
            dataAve.visibility = View.VISIBLE

            aveVis = true



            true
        }



        R.id.view -> {

            true
        }

        R.id.write -> {
            //used to save all the arrays to files if the save button is pressed

            fileStep.writeText("walk =  " + infoStep.toString())
            infoStep.clear()

            fileClimb.writeText("climb =  " + infoClimb.toString())
            infoClimb.clear()

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

            var text1:TextView = findViewById(R.id.text1)
            var stepView:TextView = findViewById(R.id.stepCount)
            var climbView:TextView = findViewById(R.id.climbCount)

            stepView.text = "step"
            climbView.text = "climb"
            text1.text = "Get Ready!"

            infoStep.clear()
            sensorEventCounter = 0
            filterEventCounter = -1

            stepCounter = 0
            climbCounter = 0

            menuRawStep.clear()
            menuRawClimb.clear()

            menuAvg.clear()
            menuMaxMin.clear()

            arrayAdapter?.notifyDataSetChanged()
            arrayAdapterAve?.notifyDataSetChanged()
            arrayAdapterMinMax?.notifyDataSetChanged()



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


            var dataS:ListView = findViewById(R.id.listStep)
            dataS.visibility = View.INVISIBLE

            var dataC:ListView = findViewById(R.id.listClimb)
            dataC.visibility = View.INVISIBLE

            var dataAve:ListView = findViewById(R.id.ave)
            dataAve.visibility = View.INVISIBLE

            var dataMinMax:ListView = findViewById(R.id.minMax)
            dataMinMax.visibility = View.INVISIBLE

            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }

    }

    override fun onPause() {
        super.onPause()
        stepClicked = false
        climbClicked = false
        sensorMan?.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        //sensorMan?.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
    } // end on resume
} //end class
